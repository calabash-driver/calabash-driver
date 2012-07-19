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

import org.json.JSONObject;

import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;
import sh.calaba.driver.server.CalabashProxy;

public class CalabashCommandHandler extends BaseCommandHandler {

  public CalabashCommandHandler(CalabashProxy calabashProxy, WebDriverLikeRequest request) {
    super(calabashProxy, request);
  }

  @Override
  public WebDriverLikeResponse handle() throws Exception {
    String sessionId = getSessionId();
    JSONObject response =
        getCalabashProxy().redirectMessageToCalabashServer(getRequest().getPayload(), sessionId);

    int responseStatusCode;
    if (response.getBoolean("success")) {
      responseStatusCode = 0;
    } else {
      responseStatusCode = 13;
    }

    return new WebDriverLikeResponse(sessionId, responseStatusCode, response);
  }

}
