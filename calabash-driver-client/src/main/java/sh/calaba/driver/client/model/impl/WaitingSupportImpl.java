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
import sh.calaba.driver.model.WaitingSupport;

/**
 * Default {@link WaitingSupport} implementation.
 * 
 * @author ddary
 */
public class WaitingSupportImpl extends RemoteObject implements WaitingSupport {

  public WaitingSupportImpl(RemoteCalabashAndroidDriver driver) {
    super(driver);
  }

  @Override
  public void waitForProgressCloses() {
    executeCalabashCommand(CalabashCommands.WAIT_FOR_NO_PROGRESS_BARS);
  }

  @Override
  public void waitForCurrentDialogCloses() {
    executeCalabashCommand(CalabashCommands.WAIT_FOR_DIALOG_TO_CLOSE);
  }

  @Override
  public void waitFor(By by) {
    if (by instanceof By.Id) {
      executeCalabashCommand(CalabashCommands.WAIT_FOR_VIEW_BY_ID, by.getIdentifier());
    } else if (by instanceof By.Text) {
      executeCalabashCommand(CalabashCommands.WAIT_FOR_TEXT, by.getIdentifier());
    } else {
      throw new IllegalArgumentException("Type of by not supported now");
    }
  }

  @Override
  public void waitFor(Integer seconds) {
    executeCalabashCommand(CalabashCommands.WAIT, String.valueOf(seconds));
  }
}
