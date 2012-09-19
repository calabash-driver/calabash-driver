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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link AdbConection} implementation to use the Android sdkHandles the execution of ADB
 * commands.
 * 
 * @author ddary
 */
public class DefaultAdbConnection implements AdbConection {
  final Logger logger = LoggerFactory.getLogger(DefaultAdbConnection.class);
  public static final String ANDROID_SDK_PATH_KEY = "ANDROID_HOME";
  private String pathToAdb = null;

  public DefaultAdbConnection(String pathToAdb) {
    this.pathToAdb = pathToAdb;
  }

  /**
   * Reading the ADB path from the environment variable {@link #ANDROID_SDK_PATH_KEY}.
   */
  public DefaultAdbConnection() {
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

  /*
   * (non-Javadoc)
   * 
   * @see sh.calaba.utils.AdbConection#runProcess(java.util.List, java.lang.String, boolean)
   */
  @Override
  public String runProcess(List<String> adbParameter, String name, boolean confirmExitValue)
      throws AdbConnetionException {
    adbParameter.add(0, this.pathToAdb);
    ProcessBuilder processBuilder = new ProcessBuilder(adbParameter);
    processBuilder.redirectErrorStream(true);
    if (logger.isDebugEnabled()) {
      logger.debug("Process '" + name + "' is about to start: " + processBuilder.command());
    }
    try {
      Process process = processBuilder.start();

      if (confirmExitValue) {
        confirmExitValueIs(0, process);
      }
      String out = IOUtils.toString(process.getInputStream());
      return out;
    } catch (IOException exception) {
      logger.error("Error occured: ", exception);
      throw new AdbConnetionException("An IOException occurred when starting ADB.");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see sh.calaba.utils.AdbConection#confirmExitValueIs(int, java.lang.Process)
   */
  @Override
  public void confirmExitValueIs(int expected, Process process) {
    while (true) {
      try {
        process.waitFor();
        if (logger.isDebugEnabled()) {
          logger.debug("Waiting for process...");
        }
        break;
      } catch (InterruptedException exception) {
        // do nothing, try to wait again
      }
    }
    if (logger.isDebugEnabled()) {
      logger.debug(" process exit value: " + process.exitValue());
    }
    if (expected != process.exitValue()) {
      throw new AdbConnetionException("Exit value of process was " + process.exitValue()
          + " but expected " + expected);
    }
  }
}
