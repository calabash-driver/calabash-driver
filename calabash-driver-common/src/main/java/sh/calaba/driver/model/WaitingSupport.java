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
package sh.calaba.driver.model;

/**
 * Interface representing basic waiting operations.
 * 
 * @author ddary
 */
public interface WaitingSupport {
  /**
   * Allows to wait until the progress dialog is closed.
   */
  public void waitForProgressCloses();

  /**
   * Allows to wait until the current dialog is closed.
   */
  public void waitForCurrentDialogCloses();

  /**
   * Allows to wait using the given method.
   * 
   * @param by The locating mechanism to use.
   */
  public void waitFor(By by);

  /**
   * Allows to wait for a given time.
   * 
   * @param seconds The number of seconds to wait.
   */
  public void waitFor(Integer seconds);
}
