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
package sh.calaba.driver.client.model.impl;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.WebViewSupport;

/**
 * Default {@link WebViewSupport} implementation.
 * 
 * @author ddary
 */
public class WebViewImpl extends RemoteObject implements WebViewSupport {
  public static final String CSS = "css";
  private By.CSS css;

  public WebViewImpl(RemoteCalabashAndroidDriver driver, By.CSS css) {
    super(driver);
    this.css = css;
  }

  @Override
  public void enterText(String text) {
    executeCalabashCommand(CalabashCommands.SET_SET, CSS, css.getIdentifier(), text);
  }

  @Override
  public void click() {
    executeCalabashCommand(CalabashCommands.TOUCH, CSS, css.getIdentifier());
  }
}
