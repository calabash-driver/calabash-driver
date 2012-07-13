package sh.calaba.driver.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.android.CalabashAndroidConnector;
import sh.calaba.driver.utils.CalabashAdbCmdRunner;

public class CalabashProxy {
  private Map<String, CalabashAndroidConnector> sessionConnectors =
      new HashMap<String, CalabashAndroidConnector>();
  private Map<String, Thread> sessionInstrumentationThreads = new HashMap<String, Thread>();
  public static final int DEFAULT_CALABASH_ANDROID_LOCAL_PORT = 34777;
  private Integer localCalabashSocketPort = null;
  private final List<CalabashCapabilities> availableCapabilities =
      new ArrayList<CalabashCapabilities>();

  public CalabashProxy(CalabashNodeConfiguration nodeConfig) {
    initializeMobileDevices(nodeConfig);
  }

  public CalabashProxy(final ProxyInitializationListener listener,
      final CalabashNodeConfiguration nodeConfig) {
    new Thread(new Runnable() {
      public void run() {
        initializeMobileDevices(nodeConfig);
        listener.afterProxyInitialization();
      }
    }).run();

  }

  public String initializeSessionForCapabilities(CalabashCapabilities calabashCapabilities) {

    System.out.println("reuqested capa: " + calabashCapabilities);
    if (availableCapabilities.contains(calabashCapabilities)) {
      // is available and can be used
      String sessionId = UUID.randomUUID().toString();

      // start the connector in an own thread
      startCalabashConnector(sessionId, calabashCapabilities);

      return sessionId;
    } else {
      throw new RuntimeException("Driver does not support requested capability: "
          + calabashCapabilities);
    }
  }

  public CalabashCapabilities getSessionCapabilities(String sessionId) {
    return sessionConnectors.get(sessionId).getSessionCapabilities();
  }

  protected Integer getNextPortNumber() {
    if (localCalabashSocketPort == null) {
      localCalabashSocketPort = DEFAULT_CALABASH_ANDROID_LOCAL_PORT;
      return localCalabashSocketPort;
    } else {
      return ++localCalabashSocketPort;
    }
  }

  /**
   * initializes the capabilities and adds afterwards them to the {@link #availableCapabilities}
   * list.
   * 
   * @param capabilities the capabilities to initialize
   */
  protected void initializeMobileDevices(CalabashNodeConfiguration nodeConfig) {
    for (CalabashCapabilities capability : nodeConfig.getCapabilities()) {
      CalabashAdbCmdRunner.installAPKFile(nodeConfig.getMobileAppPath(), capability.getDeviceId());
      CalabashAdbCmdRunner.installAPKFile(nodeConfig.getMobileTestAppPath(),
          capability.getDeviceId());

      availableCapabilities.add(capability);
    }
  }

  private Integer initializeCalabashServer(CalabashCapabilities capability, String sessionId) {
    // TODO ddary make this configurable
    CalabashAdbCmdRunner.deleteSavedAppData("com.ebay.mobile", capability.getDeviceId());
    CalabashAdbCmdRunner.switchToEbayQA(capability.getDeviceId());
    Thread instThread = CalabashAdbCmdRunner.startCalabashServer(capability.getDeviceId());
    sessionInstrumentationThreads.put(sessionId, instThread);

    Integer portNumber = getNextPortNumber();
    CalabashAdbCmdRunner.activatePortForwarding(portNumber, 7101, capability.getDeviceId());
    System.out.println("Capability initialized: " + capability.getDeviceName() + " on local port: "
        + portNumber);
    return portNumber;
  }

  private void startCalabashConnector(final String sessionId, final CalabashCapabilities capa) {
    Integer portNumber = initializeCalabashServer(capa, sessionId);

    CalabashAndroidConnector calabashConnector =
        new CalabashAndroidConnector("127.0.0.1", portNumber, capa);
    calabashConnector.startConnector();
    sessionConnectors.put(sessionId, calabashConnector);
  }

  public void stopCalabashConnector(String sessionId) {
    if (sessionConnectors.containsKey(sessionId)) {
      sessionConnectors.get(sessionId).quit();
    } else {
      throw new RuntimeException("Session not found to stop Calabash Connector: " + sessionId);
    }
    if (sessionInstrumentationThreads.containsKey(sessionId)) {
      sessionInstrumentationThreads.get(sessionId).interrupt();
    } else {
      throw new RuntimeException(
          "Session not found to kill calabash istrumentation process/thread: " + sessionId);
    }
    sessionConnectors.remove(sessionId);
    sessionInstrumentationThreads.remove(sessionId);
  }

  public JSONObject redirectMessageToCalabashServer(JSONObject command, String sessionId) {
    System.out.println("received command: " + command.toString());
    System.out.println("received sessionId: " + sessionId);
    if (sessionConnectors.containsKey(sessionId)) {
      JSONObject result = null;
      try {
        result = sessionConnectors.get(sessionId).execute(command);
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }

      return result;
    } else {
      throw new RuntimeException("Calabash Connector for Session not found: " + sessionId);
    }
  }
}
