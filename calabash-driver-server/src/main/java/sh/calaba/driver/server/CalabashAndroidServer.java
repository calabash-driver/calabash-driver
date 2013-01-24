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

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.calaba.driver.exceptions.CalabashException;
import sh.calaba.driver.server.servlet.CalabashServlet;

/**
 * The main calabash android driver server.
 * 
 * @author ddary
 * 
 */
public class CalabashAndroidServer {
  final static Logger logger = LoggerFactory.getLogger(CalabashAndroidServer.class);
  public static final String COMMAND_LINE_CONFIG_FILE_PATH = "-driverConfig";
  public static final String COMMAND_LINE_CONFIG_FILE_URI = "-driverConfigURI";
  /** This basically means the grid controls fully this driver and knows what he does */
  public static final String COMMAND_LINE_GRID_CONFIG = "-gridConfig";

  public static final String SCRIPT_KEY = CalabashProxy.class.getName();
  private Server server;
  private CalabashNodeConfiguration config;
  private boolean isReady = false;
  private CalabashProxy proxy = null;

  public static void main(String[] args) {
    if ((args == null || args.length <= 1)
        || (args != null && (!(args[0].equals(COMMAND_LINE_CONFIG_FILE_PATH)
            || args[0].equals(COMMAND_LINE_CONFIG_FILE_URI) || args[0]
              .equals(COMMAND_LINE_GRID_CONFIG))))) {
      System.out.println("#### Calabash Driver #####");
      System.out.println("ERROR: command line parameter missing!");
      System.out.println("   Please use:");
      System.out.println("     " + COMMAND_LINE_CONFIG_FILE_PATH
          + " filename.json    --> where 'filename.json' is your configuration file");
      System.out.println("   or use:");
      System.out
          .println("     "
              + COMMAND_LINE_CONFIG_FILE_URI
              + " filename.json    --> where 'http://yourhost/your/path/filename.json' is your configuration file");
      System.out.println("   or use:");
      System.out
          .println("     "
              + COMMAND_LINE_GRID_CONFIG
              + " Which basically means the grid controls fully this driver and knows what he does.");

      System.exit(0);
    }

    CalabashAndroidServer server = new CalabashAndroidServer();
    try {
      CalabashNodeConfiguration nodeConfiguration = null;
      if (COMMAND_LINE_CONFIG_FILE_PATH.equals(args[0])) {
        nodeConfiguration = CalabashNodeConfiguration.readFromFile(new File(args[1]));
      } else if (COMMAND_LINE_CONFIG_FILE_URI.equals(args[0])) {
        nodeConfiguration = CalabashNodeConfiguration.readFromURI(new URI(args[1]));
      }else if (COMMAND_LINE_GRID_CONFIG.equals(args[0])) {
        nodeConfiguration = CalabashNodeConfiguration.readFromURI(new URI(args[1]));
      }
      server.start(nodeConfiguration);
    } catch (Exception e) {
      System.out.println("An error occured starting the CalabashDriver:");
      System.out.println(e.getMessage());
      System.exit(0);
    }
  }

  public void start(CalabashNodeConfiguration config) throws Exception {
    this.config = config;

    server = new Server(config.getDriverPort());

    ServletContextHandler servletContextHandler =
        new ServletContextHandler(server, "/wd/hub", true, false);

    servletContextHandler.addServlet(CalabashServlet.class, "/*");
    List<ProxyInitializationListener> listeners = new ArrayList<ProxyInitializationListener>();
    listeners.add(new ProxyInitializationListener() {

      @Override
      public void afterProxyInitialization() {
        isReady = true;
        if (logger.isDebugEnabled()) {
          logger.debug("The CalabashAndroidServer is initialized.");
        }
      }
    });

    if (config.isDriverRegistrationEnabled()) {
      listeners.add(new ProxyInitializationListener() {

        @Override
        public void afterProxyInitialization() {
          registerDriverNodeInHub();
        }
      });
    }
    if (proxy == null) {
      setProxy(new CalabashProxy());
    }
    proxy.initializeMobileDevices(listeners, config);
    servletContextHandler.setAttribute(SCRIPT_KEY, proxy);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          server.stop();
        } catch (Exception e) {
          logger.error("An error occured while shutting down the calabash driver server: ", e);
        }
      }
    });
    server.start();
  }

  public void stop() throws Exception {
    server.stop();
  }

  public boolean isReady() {
    return server.isStarted() && isReady;
  }

  public void registerDriverNodeInHub() {
    try {
      new DriverRegistrationHandler(config).performRegsitration();
    } catch (Exception e) {
      String msg = "An error occured while registering the driver into the grid hub:";
      logger.error(msg, e);
      throw new CalabashException(msg, e);
    }
  }

  /**
   * @param proxy the proxy to set
   */
  public void setProxy(CalabashProxy proxy) {
    this.proxy = proxy;
  }
}
