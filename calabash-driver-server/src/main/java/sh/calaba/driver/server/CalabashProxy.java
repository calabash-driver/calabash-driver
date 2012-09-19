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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.exceptions.CalabashException;
import sh.calaba.driver.server.connector.CalabashAndroidConnector;
import sh.calaba.driver.server.connector.CalabashConnecterException;
import sh.calaba.driver.server.connector.impl.CalabashAndroidConnectorImpl;
import sh.calaba.driver.utils.CalabashAdbCmdRunner;

/**
 * Proxy to handle the full life cycle of interacting with the mobile device.
 * 
 * @author ddary
 * 
 */
public class CalabashProxy {
  final Logger logger = LoggerFactory.getLogger(CalabashProxy.class);
  private Map<String, CalabashAndroidConnector> sessionConnectors =
      new HashMap<String, CalabashAndroidConnector>();
  private Map<String, Thread> sessionInstrumentationThreads = new HashMap<String, Thread>();
  public static final int DEFAULT_CALABASH_ANDROID_LOCAL_PORT = 34777;
  private Integer localCalabashSocketPort = null;
  private final List<CalabashCapabilities> availableCapabilities =
      new ArrayList<CalabashCapabilities>();
  private boolean cleanSavedUserData = true;
  private CalabashAdbCmdRunner calabashAdbCmdRunner = new CalabashAdbCmdRunner();

  /**
   * Default constructor that {@link #doIinitializeMobileDevices(CalabashNodeConfiguration)}.
   * 
   * @param nodeConfig The node configuration to use.
   */
  public void initializeMobileDevices(CalabashNodeConfiguration nodeConfig) {
    doIinitializeMobileDevices(nodeConfig);
  }

  /**
   * Constructor that {@link #doIinitializeMobileDevices(CalabashNodeConfiguration)} and is
   * notifying afterwards the provided listener
   * {@link ProxyInitializationListener#afterProxyInitialization()} about the completed
   * initialization.
   * 
   * @param listener
   * @param nodeConfig
   */
  public void initializeMobileDevices(final List<ProxyInitializationListener> listeners,
      final CalabashNodeConfiguration nodeConfig) {
    new Thread(new Runnable() {
      public void run() {
        doIinitializeMobileDevices(nodeConfig);
        if (listeners != null && !listeners.isEmpty()) {
          for (ProxyInitializationListener listener : listeners)
            listener.afterProxyInitialization();
        }
      }
    }).run();
  }

  /**
   * Initializes a new test session for provided {@link CalabashCapabilities}. This means the
   * calabash-server on the device is started by running the Android instrumentation and if the test
   * server on the device is successfully started, a connection to this server is established.
   * 
   * @see #startCalabashServerAndStartConnector(String, CalabashCapabilities)
   * 
   * @param calabashCapabilities The {@link CalabashCapabilities} to use to start the test session.
   * @return The session ID of the new created session.
   */
  public String initializeSessionForCapabilities(CalabashCapabilities calabashCapabilities) {
    if (logger.isDebugEnabled()) {
      logger.debug("reqqested capa: " + calabashCapabilities);
    }
    if (availableCapabilities.contains(calabashCapabilities)) {
      // is available and can be used
      String sessionId = UUID.randomUUID().toString();

      // start the connector in an own thread
      startCalabashServerAndStartConnector(sessionId, calabashCapabilities);

      return sessionId;
    } else {
      throw new CalabashException("Driver does not support requested capability: "
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
  private void doIinitializeMobileDevices(CalabashNodeConfiguration nodeConfig) {
    this.cleanSavedUserData = nodeConfig.isCleanSavedUserDataEnabled();
    for (CalabashCapabilities capability : nodeConfig.getCapabilities()) {
      if (nodeConfig.isInstallApksEnabled()) {
        calabashAdbCmdRunner
            .installAPKFile(nodeConfig.getMobileAppPath(), capability.getDeviceId());
        calabashAdbCmdRunner.installAPKFile(nodeConfig.getMobileTestAppPath(),
            capability.getDeviceId());
      }
      availableCapabilities.add(capability);
    }
  }

  /**
   * initializes the calabash server with starting the instrumentation. Before doing this the saved
   * user data of the {@link CalabashCapabilities#getAppBasePackage()} are deleted and the
   * #adbCommands are executed. Please note that every entry in the list will be taken as complete
   * parameter list for executing an <em>adb</em> command.
   * 
   * @param capability The capability to use to start the calabash server.
   * @param sessionId The newly created session id.
   * @return The port number used for the current session.
   */
  private Integer initializeCalabashServer(CalabashCapabilities capability, String sessionId) {
    if (cleanSavedUserData) {
      String basePackage = capability.getAppBasePackage();
      String device = capability.getDeviceId();
      calabashAdbCmdRunner.deleteSavedAppData(basePackage, device);
    }
    List<String> adbCommands = capability.getAdditionalAdbCommands();
    if (adbCommands != null && !adbCommands.isEmpty()) {
      for (String adbCommandParameter : adbCommands) {
        if (logger.isDebugEnabled()) {
          logger.debug("executing adb with parameter: " + adbCommandParameter);
        }
        calabashAdbCmdRunner.executeAdbCommand(capability.getDeviceId(), adbCommandParameter);
      }
    }

    Thread instThread =
        calabashAdbCmdRunner.startCalabashServer(capability.getDeviceId(),
            capability.getAppBasePackage());
    sessionInstrumentationThreads.put(sessionId, instThread);

    Integer portNumber = getNextPortNumber();
    calabashAdbCmdRunner.activatePortForwarding(portNumber,
        CalabashAdbCmdRunner.CALABASH_INTERNAL_PORT, capability.getDeviceId());
    if (logger.isDebugEnabled()) {
      logger.debug("Capability initialized: " + capability.getDeviceName() + " on local port: "
          + portNumber);
    }
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
      throw new CalabashConnecterException("Session not found to stop Calabash Connector: "
          + sessionId);
    }
    if (sessionInstrumentationThreads.containsKey(sessionId)) {
      sessionInstrumentationThreads.get(sessionId).interrupt();
    } else {
      throw new CalabashConnecterException(
          "Session not found to kill calabash istrumentation process/thread: " + sessionId);
    }
    sessionConnectors.remove(sessionId);
    sessionInstrumentationThreads.remove(sessionId);
  }

  /**
   * Redirects the given calabash <code>command</code> to the calabash server of the corresponding
   * session. The command handling of taking screenshots is different from the other calabash
   * command because another URI is used.
   * 
   * @param command The command to redirect and execute.
   * @param sessionId The test session id.
   * @return The response of the calabash server running on the device.
   */
  public JSONObject redirectMessageToCalabashServer(JSONObject command, String sessionId) {
    if (logger.isDebugEnabled()) {
      logger.debug("received command: " + command.toString());
      logger.debug("received sessionId: " + sessionId);
    }
    if (sessionConnectors.containsKey(sessionId)) {
      JSONObject result = null;
      try {
        if ("take_screenshot_embed".equals(command.get("command"))) {
          result = sessionConnectors.get(sessionId).takeScreenshot();
        } else {
          result = sessionConnectors.get(sessionId).execute(command);
        }
      } catch (JSONException e) {
        logger.error("Exception occured: ", e);
        throw new CalabashConnecterException(
            "Json exception occured while executing calabash commands: ", e);
      } catch (IOException e) {
        logger.error("Exception occured: ", e);
        throw new CalabashConnecterException(
            "IOException exception occured while executing calabash commands: ", e);
      }

      return result;
    } else {
      throw new CalabashConnecterException("Calabash Connector for Session not found: " + sessionId);
    }
  }

  /**
   * @param calabashAdbCmdRunner the calabashAdbCmdRunner to set
   */
  public void setCalabashAdbCmdRunner(CalabashAdbCmdRunner calabashAdbCmdRunner) {
    this.calabashAdbCmdRunner = calabashAdbCmdRunner;
  }
}
