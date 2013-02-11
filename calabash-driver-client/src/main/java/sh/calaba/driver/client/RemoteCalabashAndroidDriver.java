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

import java.util.Map;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.client.model.impl.ButtonImpl;
import sh.calaba.driver.client.model.impl.DeviceImpl;
import sh.calaba.driver.client.model.impl.L10nElementImpl;
import sh.calaba.driver.client.model.impl.ListItemImpl;
import sh.calaba.driver.client.model.impl.NativeSearchImpl;
import sh.calaba.driver.client.model.impl.SpinnerImpl;
import sh.calaba.driver.client.model.impl.TextFieldImpl;
import sh.calaba.driver.client.model.impl.ViewImpl;
import sh.calaba.driver.client.model.impl.WaitingSupportImpl;
import sh.calaba.driver.client.model.impl.WebViewImpl;
import sh.calaba.driver.model.ButtonSupport;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.By.CSS;
import sh.calaba.driver.model.CalabashAndroidDriver;
import sh.calaba.driver.model.DeviceSupport;
import sh.calaba.driver.model.L10nSupport;
import sh.calaba.driver.model.ListItemSupport;
import sh.calaba.driver.model.SpinnerSupport;
import sh.calaba.driver.model.TextFieldSupport;
import sh.calaba.driver.model.ViewSupport;
import sh.calaba.driver.model.WaitingSupport;
import sh.calaba.driver.model.WebViewSupport;

/**
 * 
 * @see CalabashDriver
 * @author ddary
 * 
 */
public class RemoteCalabashAndroidDriver extends CalabashAndroidDriver
    implements
      CalabashDriver,
      CalabashWebviewDriver {
  public RemoteCalabashAndroidDriver(String host, Integer port, CalabashCapabilities capa) {
    super("http://" + host + ":" + port + "/wd/hub", capa.getRawCapabilities());
  }

  public RemoteCalabashAndroidDriver(String remoteURL, Map<String, Object> capabilities) {
    super(remoteURL, capabilities);
  }

  /**
   * Find the {@link TextFieldSupport} using the given method.
   * 
   * @param by The locating mechanism to use.
   * @return The first matching element on the current page dialogue.
   */
  public TextFieldSupport findTextField(By by) {
    return new TextFieldImpl(this, by);
  }

  /**
   * Find the {@link ButtonSupport} using the given method.
   * 
   * @param by The locating mechanism to use.
   * @return The {@link ButtonSupport} for element interactions.
   */
  public ButtonSupport findButton(By by) {
    return new ButtonImpl(this, by);
  }

  /**
   * Find the {@link SpinnerSupport} using the given method.
   * 
   * @param by The locating mechanism to use
   * @return The {@link SpinnerSupport} for element interactions.
   */
  public SpinnerSupport findSpinner(By by) {
    return new SpinnerImpl(this, by);
  }

  /**
   * Find the {@link WebViewSupport} using the given method.
   * 
   * @param css The locating mechanism to use.
   * @return The {@link WebViewSupport} for element interactions.
   * @deprecated use instead the {@link CalabashWebviewDriver} interface
   */
  @Deprecated
  public WebViewSupport webview(By.CSS css) {
    return findWebElementBy(css);
  }

  /**
   * Find the {@link ListItemSupport} using the given method.
   * 
   * @param by The locating mechanism to use.
   * @return The {@link ListItemSupport} for element interactions.
   */
  public ListItemSupport findListItem(By by) {
    return new ListItemImpl(this, by);
  }

  /**
   * @return The {@link WaitingSupport} for element interactions.
   */
  public WaitingSupport waitForElement() {
    return new WaitingSupportImpl(this);
  }

  /**
   * Find the {@link ViewSupport} using the given ID.
   * 
   * @param id The Android native name/ id of the element.
   * @return The {@link ViewSupport} for element interactions.
   */
  public ViewSupport findViewById(String id) {
    return new ViewImpl(this, (By.Id) By.id(id));
  }

  /**
   * Find the {@link L10nSupport} using the given method.
   * 
   * @param id The l10n id used in the Android App resource bundle.
   * @return The {@link L10nSupport} for element interactions.
   */
  public L10nSupport findL10nElement(By.L10nElement id) {
    return new L10nElementImpl(this, id);
  }

  /**
   * Returns the {@link DeviceSupport} for basic device interaction operations.
   */
  public DeviceSupport device() {
    return new DeviceImpl(this);
  }

  /**
   * Find the {@link TextFieldSupport} using the given method.
   * 
   * @param by The locating mechanism to use.
   * @param searchTerm The term to search for.
   */
  public void performSearch(By by, String searchTerm) {
    new NativeSearchImpl(this, (By.Id) by).text(searchTerm);
  }

  /**
   * Waits until the given text is displayed on the screen.
   * 
   * @param text The text to wait for.
   */
  public void waitForTextIsPresent(String text) {
    new WaitingSupportImpl(this).waitFor(By.text(text));
  }

  /**
   * Waits until the UI element given is found.
   * 
   * @param id The Android native name/ id of the element.
   * @return The {@link ViewSupport} for element interactions.
   */
  public ViewSupport waitForViewIsPresent(String id) {
    By.Id by = By.id(id);
    new WaitingSupportImpl(this).waitFor(by);
    return new ViewImpl(this, by);
  }

  @Override
  public WebViewSupport findWebElementBy(CSS css) {
    return new WebViewImpl(this, css);
  }

  @Override
  public String getPageSource() {
    return new WebViewImpl(this).getPageSource();
  }

  @Override
  public String getCurrentUrl() {
    return new WebViewImpl(this).getCurrentUrl();
  }
}
