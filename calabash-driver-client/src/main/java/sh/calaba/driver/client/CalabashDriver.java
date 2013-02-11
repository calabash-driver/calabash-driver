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
package sh.calaba.driver.client;

import sh.calaba.driver.model.ButtonSupport;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.DeviceSupport;
import sh.calaba.driver.model.L10nSupport;
import sh.calaba.driver.model.ListItemSupport;
import sh.calaba.driver.model.SpinnerSupport;
import sh.calaba.driver.model.TextFieldSupport;
import sh.calaba.driver.model.ViewSupport;
import sh.calaba.driver.model.WaitingSupport;


/**
 * {@link RemoteCalabashAndroidDriver} is a remote Calabash-Driver client to interact with a
 * Calabash-Driver-Server. For load balancing in between the Selenium Grid2 hub can be used for load
 * balancing purposes.
 * 
 * After initializing the {@link RemoteCalabashAndroidDriver}, automatically a new test session is
 * started. The driver offers different ways to interact with the Android Application.
 * 
 * The main idea of interacting with the different kind of elements is by different locators (called
 * {@link By}) and by different UI elements types.
 * 
 * Important: Currently not all available calabash-android commands are implemented. The reason for
 * this is that that we have only the commands implemented that we need for our testing. Adding the
 * other commands is easy and will be propably be done in the near future.
 * 
 * @author ddary
 * 
 */
public interface CalabashDriver {

  /**
   * Find the {@link TextFieldSupport} using the given method.
   * 
   * @param by The locating mechanism to use.
   * @return The first matching element on the current page dialogue.
   */
  public TextFieldSupport findTextField(By by);

  /**
   * Find the {@link ButtonSupport} using the given method.
   * 
   * @param by The locating mechanism to use.
   * @return The {@link ButtonSupport} for element interactions.
   */
  public ButtonSupport findButton(By by);

  /**
   * Find the {@link SpinnerSupport} using the given method.
   * 
   * @param by The locating mechanism to use
   * @return The {@link SpinnerSupport} for element interactions.
   */
  public SpinnerSupport findSpinner(By by);

  /**
   * Find the {@link ListItemSupport} using the given method.
   * 
   * @param by The locating mechanism to use.
   * @return The {@link ListItemSupport} for element interactions.
   */
  public ListItemSupport findListItem(By by);

  /**
   * @return The {@link WaitingSupport} for element interactions.
   */
  public WaitingSupport waitForElement();

  /**
   * Find the {@link ViewSupport} using the given ID.
   * 
   * @param id The Android native name/ id of the element.
   * @return The {@link ViewSupport} for element interactions.
   */
  public ViewSupport findViewById(String id);

  /**
   * Find the {@link L10nSupport} using the given method.
   * 
   * @param id The l10n id used in the Android App resource bundle.
   * @return The {@link L10nSupport} for element interactions.
   */
  public L10nSupport findL10nElement(By.L10nElement id);

  /**
   * Returns the {@link DeviceSupport} for basic device interaction operations.
   */
  public DeviceSupport device();

  /**
   * Find the {@link TextFieldSupport} using the given method.
   * 
   * @param by The locating mechanism to use.
   * @param searchTerm The term to search for.
   */
  public void performSearch(By by, String searchTerm);

  /**
   * Waits until the given text is displayed on the screen.
   * 
   * @param text The text to wait for.
   */
  public void waitForTextIsPresent(String text);

  /**
   * Waits until the UI element given is found.
   * 
   * @param id The Android native name/ id of the element.
   * @return The {@link ViewSupport} for element interactions.
   */
  public ViewSupport waitForViewIsPresent(String id);

}
