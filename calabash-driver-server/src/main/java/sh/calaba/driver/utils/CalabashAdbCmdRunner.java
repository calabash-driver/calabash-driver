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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import sh.calaba.utils.AdbConnection;
/**
 * Central place to execute Calabash ADB commands.
 * @author ddary
 *
 */
public class CalabashAdbCmdRunner {
  private static AdbConnection adbConnection = getAdbConnection();

  protected static AdbConnection getAdbConnection() {
    return new AdbConnection();
  }

  /**
   * TODO ddary: make this configurable
   * 
   * @param deviceId The device id to use to install the device.
   */
  public static void switchToEbayQA(String deviceId) {
    List<String> commandLineFwd = new ArrayList<String>();
    if (deviceId != null) {
      commandLineFwd.add("-s");
      commandLineFwd.add(deviceId);
    }
    commandLineFwd.add("shell");
    commandLineFwd.add("setprop");
    // USE THIS for prior 1.8
    // commandLineFwd.add("log.tag.eBayQAServerSwitch");

    // Use this for post 1.8 releases
    commandLineFwd.add("log.tag.fwUseQaServers");


    commandLineFwd.add("DEBUG");

    adbConnection.runProcess(commandLineFwd, "Switching to eBay QA Env", true);
  }

  /**
   * Allows to install an APK file on the given device.
   * @param pathToAPK The path to the APK file to install.
   * @param deviceId The device id to use to install the device.
   */
  public static void installAPKFile(String pathToAPK, String deviceId) {
    List<String> commandLineFwd = new ArrayList<String>();
    if (deviceId != null) {
      commandLineFwd.add("-s");
      commandLineFwd.add(deviceId);
    }
    commandLineFwd.add("install");
    commandLineFwd.add("-r");
    commandLineFwd.add(pathToAPK);

    adbConnection.runProcess(commandLineFwd, "about to start install APK", true);
  }

  /**
   * Allows to start the calabash server on the given device.
   * @param deviceId The device id to use to start the server.
   * @return The thread that has started the Android instrumentation.
   */
  public static Thread startCalabashServer(final String deviceId) {
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
        commandLineFwd.add("com.ebay.mobile.test/android.test.InstrumentationTestRunner");

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
   * @param deviceId The device id to use.
   */
  public static void waitForCalabashServerOnDevice(final String deviceId) {
    (new CalabashAdbCmdRunner().new CalabashServerWaiter(adbConnection, deviceId)).run();;
  }

  /**
   * Forwards the given remote port of the calabash server to the given local port.
   * @param local The local port to use.
   * @param remote The remote port on the device to use.
   * @param deviceId The device id to use to activate the port forwarding.
   */
  public static void activatePortForwarding(int local, int remote, String deviceId) {
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
   * Allows all the user data of the app for the given <code>appBasePackage</code>
   * @param appBasePackage The base package of the app to delete the user data for.
   * @param deviceId The device id to use.
   */
  public static void deleteSavedAppData(String appBasePackage, String deviceId) {
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
   * Runnable implementation to wait until the calabash server is started on the device.
   * @author ddary
   */
  public class CalabashServerWaiter implements Runnable {
    private Lock lock = new ReentrantLock();
    private Condition cv = lock.newCondition();
    private AdbConnection adbConnection;
    private String deviceId;

    CalabashServerWaiter(AdbConnection con, String deviceId) {
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
      } else if (result.contains(":::7101")) {
        return true;
      } else {
        return false;
      }
    }

    @Override
    public void run() {
      lock.lock();

      try {
        boolean portIsNotBound = !isPortBound();
        while (portIsNotBound) {

          cv.await(2, TimeUnit.SECONDS);
          portIsNotBound = !isPortBound();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }
}
