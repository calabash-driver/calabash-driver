/*
 * Copyright 2012 calabash-driver committers.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
import sh.calaba.driver.server.connector.CalabashAndroidConnector;
import sh.calaba.driver.server.connector.impl.CalabashAndroidConnectorImpl;
import sh.calaba.driver.utils.CalabashAdbCmdRunner;

/**
 * Proxy to handle the full life cycle of interacting with the mobile device.
 * 
 * @author ddary
 * 
 */
public class CalabashProxy {
  private Map<String, CalabashAndroidConnector> sessionConnectors =
      new HashMap<String, CalabashAndroidConnector>();
  private Map<String, Thread> sessionInstrumentationThreads = new HashMap<String, Thread>();
  public static final int DEFAULT_CALABASH_ANDROID_LOCAL_PORT = 34777;
  private Integer localCalabashSocketPort = null;
  private final List<CalabashCapabilities> availableCapabilities =
      new ArrayList<CalabashCapabilities>();

  /**
   * Default constructor that {@link #initializeMobileDevices(CalabashNodeConfiguration)}.
   * 
   * @param nodeConfig The node config to use.
   */
  public CalabashProxy(CalabashNodeConfiguration nodeConfig) {
    initializeMobileDevices(nodeConfig);
  }

  /**
   * Constructor that {@link #initializeMobileDevices(CalabashNodeConfiguration)} and is notifying
   * afterwards the provided listener {@link ProxyInitializationListener#afterProxyInitialization()}
   * about the completed initialization.
   * 
   * @param listener
   * @param nodeConfig
   */
  public CalabashProxy(final ProxyInitializationListener listener,
      final CalabashNodeConfiguration nodeConfig) {
    new Thread(new Runnable() {
      public void run() {
        initializeMobileDevices(nodeConfig);
        listener.afterProxyInitialization();
      }
    }).run();
  }

  /**
   * Initializes a new test session for provided {@link CalabashCapabilities}. This means the
   * calabash-server on the device is started by starting the Android instrumentation and if the
   * test server in the device is successfully started, a connected to this server is established.
   * 
   * @see #startCalabashServerAndStartConnector(String, CalabashCapabilities)
   * 
   * @param calabashCapabilities The {@link CalabashCapabilities} to use to start the test session.
   * @return The session ID of the new created session.
   */
  public String initializeSessionForCapabilities(CalabashCapabilities calabashCapabilities) {
    System.out.println("reuqested capa: " + calabashCapabilities);
    if (availableCapabilities.contains(calabashCapabilities)) {
      // is available and can be used
      String sessionId = UUID.randomUUID().toString();

      // start the connector in an own thread
      startCalabashServerAndStartConnector(sessionId, calabashCapabilities);

      return sessionId;
    } else {
      throw new RuntimeException("Driver does not support requested capability: "
          + calabashCapabilities);
    }
  }

  /**
   * Get the {@link CalabashCapabilities} of the provided session.
   * 
   * @param sessionId The session id of the session.
   * @return The {@link CalabashCapabilities}.
   */
  public CalabashCapabilities getSessionCapabilities(String sessionId) {
    return sessionConnectors.get(sessionId).getSessionCapabilities();
  }

  /**
   * @return The next free port number that will be used on the local computer.
   */
  protected Integer getNextPortNumber() {
    if (localCalabashSocketPort == null) {
      localCalabashSocketPort = DEFAULT_CALABASH_ANDROID_LOCAL_PORT;
      return localCalabashSocketPort;
    } else {
      return ++localCalabashSocketPort;
    }
  }

  /**
   * Initializes the capabilities and adds afterwards them to the {@link #availableCapabilities}
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

  /**
   * Convenient method to start the calabash-server on the device and connect to it afterwards. The
   * connector is initialized using the
   * {@link #initCalabashConnector(Integer, CalabashCapabilities)} method.
   * 
   * @param sessionId The session id to use.
   * @param capa The session capabilities.
   */
  private void startCalabashServerAndStartConnector(String sessionId, CalabashCapabilities capa) {
    Integer portNumber = initializeCalabashServer(capa, sessionId);

    CalabashAndroidConnector calabashConnector = initCalabashConnector(portNumber, capa);

    calabashConnector.start();
    sessionConnectors.put(sessionId, calabashConnector);
  }

  /**
   * Initializes the default Calabash-Connector implementation.
   * 
   * @param portNumber The port number to use. This is the one that is use on the local computer and
   *        not the port number on the device itself.
   * @param capa The capabilties used for this session.
   * 
   * @return The initialized connector, which is not yet started.
   */
  protected CalabashAndroidConnector initCalabashConnector(Integer portNumber,
      CalabashCapabilities capa) {
    return new CalabashAndroidConnectorImpl("127.0.0.1", portNumber, capa);
  }

  /**
   * Stops the calabash connector for the given session.
   * 
   * @param sessionId The session id of the session to stop.
   */
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

  /**
   * Redirects the given calabash <code>command</code> to the calabash server of the corresponding
   * session.
   * 
   * @param command The command to redirect and execute.
   * @param sessionId The test session id.
   * @return The response of the calabash server running on the device.
   */
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
