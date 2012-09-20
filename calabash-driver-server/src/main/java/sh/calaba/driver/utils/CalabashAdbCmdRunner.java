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
package sh.calaba.driver.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.calaba.driver.exceptions.CalabashException;
import sh.calaba.utils.AdbConection;
import sh.calaba.utils.DefaultAdbConnection;

/**
 * Central place to execute Calabash ADB commands.
 * 
 * @author ddary
 * 
 */
public class CalabashAdbCmdRunner {
  final static Logger logger = LoggerFactory.getLogger(CalabashAdbCmdRunner.class);
  private AdbConection adbConnection = null;
  public static final String CALABASH_DRIVER_APPS = "CALABASH_DRIVER_APPS";
  private static String pathToDriverApps = null;
  public static final int CALABASH_INTERNAL_PORT = 7102;

  public CalabashAdbCmdRunner() {
    this.adbConnection = new DefaultAdbConnection();
  }

  public CalabashAdbCmdRunner(AdbConection adbConnection) {
    this.adbConnection = adbConnection;
  }

  /**
   * @return The path to the apps folder, where the APK files are located.
   */
  protected String getPathToDriverApps() {
    if (pathToDriverApps == null) {
      pathToDriverApps = System.getenv(CALABASH_DRIVER_APPS);
      if (pathToDriverApps == null) {
        logger.error("Environment variable " + CALABASH_DRIVER_APPS + " is not configured.");
        throw new CalabashException("The environment variable '" + CALABASH_DRIVER_APPS
            + "' seems not to be configured. "
            + "Please make sure this is properly configured on your machine.");
      }
      if (!pathToDriverApps.endsWith(File.separator)) {
        pathToDriverApps = pathToDriverApps.concat(File.separator);
      }
    }
    return pathToDriverApps;
  }

  /**
   * Executes the android adb program for the specified deviceId and with the given adbParameters.
   * 
   * @param deviceId The device id to use to install the device.
   * @param adbParamter The parameter to use to execute the adb command.
   * @throws {@link IllegalArgumentException} if adbParameter are not existent.
   */
  public void executeAdbCommand(String deviceId, String adbParamter) {
    if (adbParamter == null || adbParamter.isEmpty()) {
      throw new IllegalArgumentException("adbParamter must not be null!'");
    }
    List<String> commandLineFwd = new ArrayList<String>();
    if (deviceId != null) {
      commandLineFwd.add("-s");
      commandLineFwd.add(deviceId);
    }
    // It is very important to add every argument as separate string.
    commandLineFwd.addAll(Arrays.asList(adbParamter.split(" ")));

    adbConnection.runProcess(commandLineFwd, "Executing adb Command", true);
  }

  /**
   * Allows all the user data of the app for the given <code>appBasePackage</code>
   * 
   * @param appBasePackage The base package of the app to delete the user data for.
   * @param deviceId The device id to use.
   */
  public void deleteSavedAppData(String appBasePackage, String deviceId) {
    List<String> commandLineFwd = new ArrayList<String>();
    if (deviceId != null) {
      commandLineFwd.add("-s");
      commandLineFwd.add(deviceId);
    }
    commandLineFwd.add("shell");
    commandLineFwd.add("pm");
    commandLineFwd.add("clear");
    commandLineFwd.add(appBasePackage);
    adbConnection.runProcess(commandLineFwd,
        "about to delete the saved app data of the app with the base package: " + appBasePackage,
        true);
  }

  /**
   * Allows to install an APK file on the given device. The path, where the APK files are located is
   * retrieved from the system variable {@link #CALABASH_DRIVER_APPS}.
   * 
   * @param apkFileName The file name of the APK file to install.
   * @param deviceId The device id to use to install the device.
   */
  public void installAPKFile(String apkFileName, String deviceId) {
    List<String> commandLineFwd = new ArrayList<String>();
    if (deviceId != null) {
      commandLineFwd.add("-s");
      commandLineFwd.add(deviceId);
    }
    commandLineFwd.add("install");
    commandLineFwd.add("-r");
    String path = getPathToDriverApps();
    commandLineFwd.add(path + apkFileName);

    adbConnection.runProcess(commandLineFwd, "about to start install APK", true);
  }

  /**
   * Allows to start the calabash server on the given device.
   * 
   * @param deviceId The device id to use to start the server.
   * @param appBasePackageName The base package name of the app e.g. com.ebay.mobile.
   * @return The thread that has started the Android instrumentation.
   */
  public Thread startCalabashServer(final String deviceId, final String appBasePackageName) {
    Thread instrumentationThread = new Thread(new Runnable() {

      @Override
      public void run() {
        List<String> commandLineFwd = new ArrayList<String>();
        if (deviceId != null) {
          commandLineFwd.add("-s");
          commandLineFwd.add(deviceId);
        }
        commandLineFwd.add("shell");

        commandLineFwd.add("am");
        commandLineFwd.add("instrument");
        commandLineFwd.add("-e class");
        commandLineFwd.add("sh.calaba.instrumentationbackend.InstrumentationBackend");
        commandLineFwd.add("-w");
        commandLineFwd.add(appBasePackageName
            + ".test/sh.calaba.instrumentationbackend.CalabashInstrumentationTestRunner");

        adbConnection.runProcess(commandLineFwd, "about to start CalabashServer", false);

      }
    });
    instrumentationThread.start();

    // needed because the process will not end, but wait is needed
    waitForCalabashServerOnDevice(deviceId);
    return instrumentationThread;
  }

  /**
   * Allows to wait until the calabash server is started on the device.
   * 
   * @param deviceId The device id to use.
   */
  public void waitForCalabashServerOnDevice(final String deviceId) {
    (new CalabashAdbCmdRunner().new CalabashServerWaiter(adbConnection, deviceId)).run();
  }

  /**
   * Forwards the given remote port of the calabash server to the given local port.
   * 
   * @param local The local port to use.
   * @param remote The remote port on the device to use.
   * @param deviceId The device id to use to activate the port forwarding.
   */
  public void activatePortForwarding(int local, int remote, String deviceId) {
    List<String> commandLineFwd = new ArrayList<String>();
    if (deviceId != null) {
      commandLineFwd.add("-s");
      commandLineFwd.add(deviceId);
    }
    commandLineFwd.add("forward");
    commandLineFwd.add("tcp:" + local);
    commandLineFwd.add("tcp:" + remote);
    adbConnection.runProcess(commandLineFwd, "about to forward: local port: " + local
        + " to remote port: " + remote, true);
  }

  /**
   * Runnable implementation to wait until the calabash server is started on the device.
   * 
   * @author ddary
   */
  public class CalabashServerWaiter implements Runnable {
    private Lock lock = new ReentrantLock();
    private Condition cv = lock.newCondition();
    private AdbConection adbConnection;
    private String deviceId;

    CalabashServerWaiter(AdbConection con, String deviceId) {
      this.adbConnection = con;
      this.deviceId = deviceId;
    }

    private boolean isPortBound() {
      List<String> commandLineFwd = new ArrayList<String>();
      if (deviceId != null) {
        commandLineFwd.add("-s");
        commandLineFwd.add(deviceId);
      }
      commandLineFwd.add("shell");
      commandLineFwd.add("netstat");
      String result =
          adbConnection.runProcess(commandLineFwd, "wait for calabash-server on mobile device",
              true);

      if (result == null) {
        return false;
      } else if (result.contains(":" + CalabashAdbCmdRunner.CALABASH_INTERNAL_PORT)) {
        return true;
      } else {
        return false;
      }
    }

    @Override
    public void run() {
      lock.lock();

      try {
        while (!isPortBound()) {
          cv.await(2, TimeUnit.SECONDS);
        }
      } catch (InterruptedException e) {
        logger.error("Waiting Thread for calabash server was interrupted: ", e);
      } finally {
        lock.unlock();
      }
    }
  }
}
