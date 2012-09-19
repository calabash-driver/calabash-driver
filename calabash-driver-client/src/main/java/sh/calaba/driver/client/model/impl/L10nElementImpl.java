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
import sh.calaba.driver.model.L10nSupport;

/**
 * Default {@link L10nSupport} implementation.
 * @author ddary
 *
 */
public class L10nElementImpl extends RemoteObject implements L10nSupport {
  private By.L10nElement id = null;

  public L10nElementImpl(RemoteCalabashAndroidDriver driver, By.L10nElement id) {
    super(driver);
    this.id = id;
  }

  @Override
  public void press() {
    if (id.getType() == null) {
      executeCalabashCommand(CalabashCommands.PRESS_L10N_ELEMENT, id.getIdentifier());
    } else {
      executeCalabashCommand(CalabashCommands.PRESS_L10N_ELEMENT, id.getIdentifier(), id.getType()
          .name());
    }
  }

  @Override
  public void waitFor() {
    executeCalabashCommand(CalabashCommands.WAIT_FOR_L10N_ELEMENT, id.getIdentifier());
  }
}
