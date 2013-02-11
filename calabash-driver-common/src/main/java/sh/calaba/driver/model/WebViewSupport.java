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
 * Interface representing basic web view interaction operations.
 * 
 * @author ddary
 */
public interface WebViewSupport {

  /**
   * Allows to enter text into a text field inside a web view.
   * 
   * @param text The text to enter.
   */
  public void enterText(String text);

  /**
   * Allows to click an element inside a web view.
   */
  public void click();
}
