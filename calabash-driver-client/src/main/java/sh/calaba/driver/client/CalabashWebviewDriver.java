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

import sh.calaba.driver.client.model.impl.WebViewImpl;
import sh.calaba.driver.model.ButtonSupport;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.DeviceSupport;
import sh.calaba.driver.model.L10nSupport;
import sh.calaba.driver.model.ListItemSupport;
import sh.calaba.driver.model.SpinnerSupport;
import sh.calaba.driver.model.TextFieldSupport;
import sh.calaba.driver.model.ViewSupport;
import sh.calaba.driver.model.WaitingSupport;
import sh.calaba.driver.model.WebViewSupport;


/**
 * {@link CalabashWebviewDriver} is a remote Calabash-Driver client to interact with a
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
public interface CalabashWebviewDriver {
  /**
   * Find the {@link WebViewSupport} using the given seletor method.
   * 
   * @param css The locating mechanism to use.
   * @return The {@link WebViewSupport} for element interactions.
   */
  public WebViewSupport findWebElementBy(By.CSS css);

  /**
   * @return The source of the current page that is loaded in the web view.
   */
  public String getPageSource();

  /**
   * Get a string representing the current URL that the web view is looking at.
   * 
   * @return The URL of the page currently loaded in the web view.
   */
  public String getCurrentUrl();

}
