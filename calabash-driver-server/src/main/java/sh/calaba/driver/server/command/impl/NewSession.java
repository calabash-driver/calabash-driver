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
package sh.calaba.driver.server.command.impl;

import org.json.JSONObject;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.command.BaseCommandHandler;

/**
 * Command handler for a new calabash test session.
 * 
 * @author ddary
 */
public class NewSession extends BaseCommandHandler {

  public NewSession(CalabashProxy proxy, WebDriverLikeRequest request) {
    super(proxy, request);
  }

  public WebDriverLikeResponse handle() throws Exception {
    JSONObject payload = getRequest().getPayload();
    // Retrieving the capabilities for the session
    JSONObject desiredCapabilities = payload.getJSONObject("desiredCapabilities");

    CalabashCapabilities capabilities = CalabashCapabilities.fromJSON(desiredCapabilities);

    JSONObject json = new JSONObject();
    String sessionID = getCalabashProxy().initializeSessionForCapabilities(capabilities);
    System.out.println("New Session Class; session iD: " + sessionID);
    json.put("sessionId", sessionID);
    json.put("status", 0);
    json.put("value", new JSONObject());
    WebDriverLikeResponse r = new WebDriverLikeResponse(json);

    System.out.println("Created session ID in response: " + r.getSessionId());
    return r;
  }
}
