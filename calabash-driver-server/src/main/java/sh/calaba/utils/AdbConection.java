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

import java.util.List;


/**
 * Connection to interact with the Android Debug Bridge.
 * 
 * @author ddary
 */
public interface AdbConection {

  /**
   * Runs the process with the given parameters.
   * 
   * @param adbParameter The adb parameters to use.
   * @param name The name of the process use for logging.
   * @param confirmExitValue If true, the process out exit value is verified.
   * @return The console output
   * @throws AdbConnetionException On adb execution issues.
   */
  public abstract String runProcess(List<String> adbParameter, String name, boolean confirmExitValue)
      throws AdbConnetionException;

  /**
   * 
   * Confirms the exit value of a process is equal to an expected value, and throws an exception if
   * it is not. This method will also wait for the process to finish before checking the exit value.
   * 
   * @param expected the expected exit value, usually {@code 0}
   * @param process the process whose exit value will be confirmed
   * @throws AdbException if the exit value was not equal to {@code expected}
   */
  public abstract void confirmExitValueIs(int expected, Process process);

}
