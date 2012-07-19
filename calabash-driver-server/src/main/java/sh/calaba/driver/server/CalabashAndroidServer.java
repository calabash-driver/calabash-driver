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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import sh.calaba.driver.server.exceptions.CalabashConfigurationException;
import sh.calaba.driver.server.servlet.CalabashServlet;

/**
 * 
 * @author ddary
 * 
 */
public class CalabashAndroidServer {
  public static final String COMMAND_LNE_PARAMETER = "-driverConfig";

  public static final String SCRIPT_KEY = CalabashProxy.class.getName();
  private final Server server;
  private CalabashNodeConfiguration config;

  public static void main(String[] args) {
    if ((args == null || args.length <= 1)
        || (args != null && !args[0].equals(COMMAND_LNE_PARAMETER))) {
      System.out.println("#### Calabash Driver #####");
      System.out.println("ERROR: command line parameter missing!");
      System.out.println("   Please use:");
      System.out.println("     " + COMMAND_LNE_PARAMETER
          + " filename.json    --> where 'filename.json' is your configuration file");
      System.exit(0);
    }
    String driverConfigurationFile = args[1];

    CalabashAndroidServer server = null;
    try {
      server =
          new CalabashAndroidServer(CalabashNodeConfiguration.readConfig(driverConfigurationFile));
    } catch (CalabashConfigurationException e) {
      System.out.println("A CalabashDriver configuartion error occured:");
      System.out.println(e.getMessage());
      System.exit(0);
    }
    try {
      server.start();
    } catch (Exception e) {
      System.out.println("An error occured starting the CalabashDriver:");
      System.out.println(e.getMessage());
      System.exit(0);
    }
  }

  public void start() throws Exception {
    server.start();
  }

  public void stop() throws Exception {
    server.stop();
  }

  public CalabashAndroidServer(CalabashNodeConfiguration config) {
    this.config = config;

    server = new Server(config.getDriverPort());

    ServletContextHandler servletContextHandler =
        new ServletContextHandler(server, "/wd/hub", true, false);

    servletContextHandler.addServlet(CalabashServlet.class, "/*");
    CalabashProxy proxy = null;
    if (config.isDriverRegistrationEnabled()) {
      proxy = new CalabashProxy(new ProxyInitializationListener() {
        public void afterProxyInitialization() {
          registerDriverNodeInHub();
        }
      }, config);
    } else {
      proxy = new CalabashProxy(config);
    }
    servletContextHandler.setAttribute(SCRIPT_KEY, proxy);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          server.stop();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void registerDriverNodeInHub() {
    try {
      new DriverRegistrationHandler(config).handleRegsitration();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
