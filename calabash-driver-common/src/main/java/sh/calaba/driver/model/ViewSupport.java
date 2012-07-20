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
 * Interface representing basic UI-view element operations.
 * 
 * @author ddary
 */
public interface ViewSupport {
  /**
   * Allows to click on an element.
   */
  public void click();

  /**
   * @return Get the visible text of this element
   */
  public String getText();

  /**
   * @return True, if the element is enabled otherwise false.
   */
  public Boolean isEnabled();

  /**
   * Get the visible text of the given <code>type</code> and the element with given index
   * <code>number</code>. This method meant to be used on dialogs where e.g. 10 TextFields are
   * displayed without having a unique id/name.
   * The example code is returning the visible text of the 6th TextField:
   * <code>
   * getText(ElementType.TextField, 6);
   * </code>
   * 
   * @param type The type of element to search for.
   * @param number The number of element to use
   * @return Get the visible text of this element
   */
  public String getText(ElementType type, Integer number);
}
