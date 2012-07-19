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
package sh.calaba.driver.server.command;

import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.Handler;

public abstract class BaseCommandHandler implements Handler {

  private final WebDriverLikeRequest request;
  private final CalabashProxy calabashProxy;

  public BaseCommandHandler(CalabashProxy calabashProxy, WebDriverLikeRequest request) {
    this.request = request;
    this.calabashProxy = calabashProxy;
  }

  protected WebDriverLikeRequest getRequest() {
    return request;
  }

  protected CalabashProxy getCalabashProxy() {
    return calabashProxy;
  }

  protected String getSessionId() {
    return getRequest().getVariableValue(":sessionId");
  }
}
