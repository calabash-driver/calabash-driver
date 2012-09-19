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
import sh.calaba.driver.model.NativeSearchSupport;
/**
 * Default {@link NativeSearchSupport} implementation.
 * 
 * @author ddary
 */
public class NativeSearchImpl extends RemoteObject implements NativeSearchSupport {
  private By.Id by;

  public NativeSearchImpl(RemoteCalabashAndroidDriver driver, By.Id id) {
    super(driver);
    this.by = id;
  }

  @Override
  public void text(String searchTerm) {
    executeCalabashCommand(CalabashCommands.SEARCH_FOR, by.getIdentifier(), searchTerm);
  }
}