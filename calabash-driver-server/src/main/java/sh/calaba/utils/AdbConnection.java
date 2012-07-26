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
package sh.calaba.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * Handles the execution of ADB commands.
 * 
 * @author ddary
 */
public class AdbConnection {
  public static final String ANDROID_SDK_PATH_KEY = "ANDROID_HOME";
  private String pathToAdb = null;

  public AdbConnection(String pathToAdb) {
    this.pathToAdb = pathToAdb;
  }

  /**
   * Reading the ADB path from the environment variable {@link #ANDROID_SDK_PATH_KEY}.
   */
  public AdbConnection() {
    String androidHome = System.getenv(ANDROID_SDK_PATH_KEY);

    if (androidHome == null) {
      throw new RuntimeException("Environment variable '" + ANDROID_SDK_PATH_KEY
          + "' was not found!");
    }
    boolean isWindows = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
    String executableSuffix = isWindows ? ".exe" : "";
    this.pathToAdb =
        androidHome + File.separator + "platform-tools" + File.separator + "adb" + executableSuffix;
  }

  /**
   * Runs the process with the given parameters.
   * 
   * @param abdParameter The adb parameters to use.
   * @param name The name of the process use for logging.
   * @param confirmExitValue If true, the process out exit value is verified.
   * @throws AdbConnetionException On adb execution issues.
   */
  public String runProcess(List<String> abdParameter, String name, boolean confirmExitValue)
      throws AdbConnetionException {
    abdParameter.add(0, this.pathToAdb);
    ProcessBuilder processBuilder = new ProcessBuilder(abdParameter);
    processBuilder.redirectErrorStream(true);
    // processBuilder.
    System.out.println("Process '" + name + "' is about to start: " + processBuilder.command());
    try {
      Process process = processBuilder.start();

      if (confirmExitValue) {
        confirmExitValueIs(0, process);
      }
      String out = IOUtils.toString(process.getInputStream());
      return out;
    } catch (IOException exception) {
      System.err.println("Error occured: ");
      throw new AdbConnetionException("An IOException occurred when starting ADB.");
    }
  }

  /**
   * 
   * Confirms the exit value of a process is equal to an expected value, and throws an exception if
   * it is not. This method will also wait for the process to finish before checking the exit value.
   * 
   * @param expected the expected exit value, usually {@code 0}
   * @param process the process whose exit value will be confirmed
   * @throws AdbException if the exit value was not equal to {@code expected}
   */
  public static void confirmExitValueIs(int expected, Process process) {
    while (true) {
      try {
        process.waitFor();
        System.out.println("Waiting for process...");
        break;
      } catch (InterruptedException exception) {
        // do nothing, try to wait again
      }
    }
    System.out.println(" process exit value: " + process.exitValue());
    if (expected != process.exitValue()) {
      throw new AdbConnetionException("Exit value of process was " + process.exitValue()
          + " but expected " + expected);
    }
  }
}
