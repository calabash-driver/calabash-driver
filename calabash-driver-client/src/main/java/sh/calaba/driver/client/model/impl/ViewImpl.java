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

import org.json.JSONObject;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.ElementType;
import sh.calaba.driver.model.ViewSupport;

/**
 * Default {@link ViewSupport} implementation.
 *
 * @author ddary
 */
public class ViewImpl extends RemoteObject implements ViewSupport {
  private By.Id id = null;

  public ViewImpl(RemoteCalabashAndroidDriver driver, By.Id id) {
    super(driver);
    this.id = id;
  }

  public ViewImpl(RemoteCalabashAndroidDriver driver) {
    super(driver);
  }

  public void click() {
    assertIdNotNull();
    executeCalabashCommand(CalabashCommands.CLICK_ON_VIEW_BY_NAME, id.getIdentifier());
  }

  private void assertIdNotNull() {
    if (id == null) {
      throw new IllegalArgumentException("Instance variable id cannot be null.");
    }
  }

  public String getText() {
    JSONObject result =
        executeCalabashCommand(CalabashCommands.GET_ELEMENT_TEXT_BY_NAME, id.getIdentifier());
    return result.optString("elementText");
  }

  @Override
  public Boolean isEnabled() {
    JSONObject result =
        executeCalabashCommand(CalabashCommands.VIEW_ENABLED_STATUS_BY_NAME, id.getIdentifier());
    return result.optBoolean("viewEnabledStatus");
  }

  public String getText(ElementType type, Integer number) {
    JSONObject result =
        executeCalabashCommand(CalabashCommands.GET_ELEMENT_TEXT_BY_TYPE, type.name(),
            String.valueOf(number));

    return result.optString("elementText");
  }
}
