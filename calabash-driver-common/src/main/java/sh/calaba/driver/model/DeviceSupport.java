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

import java.io.File;

/**
 *  Interface representing basic device interaction operations.
 * 
 * @author ddary
 */
public interface DeviceSupport {
  /**
   * Allows to scroll up the device screen.
   */
  public void scrollUp();

  /**
   * Allows to scroll down the device screen.
   */
  public void scrollDown();

  /**
   * Capture the screenshot and store it in the specified path.
   * @param path The path to safe the screenshot to.
   * @return The file name of the created screenshot.
   */
  public File takeScreenshot(String path);

  /**
   * Allows to press the context menu item.
   * @param text The text to press on.
   */
  public void pressContextMenuItem(By.ContentDescription text);
}
