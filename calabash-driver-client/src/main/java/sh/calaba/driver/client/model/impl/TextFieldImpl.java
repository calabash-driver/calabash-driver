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
import sh.calaba.driver.model.TextFieldSupport;

/**
 * Default {@link TextFieldSupport} implementation.
 * 
 * @author ddary
 */
public class TextFieldImpl extends RemoteObject implements TextFieldSupport {
  private By by;

  public TextFieldImpl(RemoteCalabashAndroidDriver driver, By by) {
    super(driver);
    this.by = by;
  }

  @Override
  public void clear() {
    if (by instanceof By.Index) {
      executeCalabashCommand(CalabashCommands.CLEAR_NUMBERED_FIELD, by.getIndentifier());
    } else if (by instanceof By.ContentDescription) {
      executeCalabashCommand(CalabashCommands.CLEAR_NAMED_FIELD, by.getIndentifier());
    } else {
      throw new IllegalArgumentException("By not available.");
    }

  }

  @Override
  public void enterText(String text) {
    if (by instanceof By.Index) {
      executeCalabashCommand(CalabashCommands.ENTER_TEXT_INTO_NUMBERED_FIELD, text,
          by.getIndentifier());
    } else if (by instanceof By.ContentDescription) {
      executeCalabashCommand(CalabashCommands.ENTER_TEXT_INTO_NAMED_FIELD, text,
          by.getIndentifier());
    } else if (by instanceof By.Id) {
      executeCalabashCommand(CalabashCommands.ENTER_TEXT_BY_NAME, text, by.getIndentifier());
    } else {
      throw new IllegalArgumentException("By not available.");
    }
  }
}
