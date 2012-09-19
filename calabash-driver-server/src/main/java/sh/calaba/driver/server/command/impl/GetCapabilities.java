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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.command.BaseCommandHandler;

public class GetCapabilities extends BaseCommandHandler {
  final Logger logger = LoggerFactory.getLogger(GetCapabilities.class);

  public GetCapabilities(CalabashProxy proxy, WebDriverLikeRequest request) {
    super(proxy, request);

  }

  public WebDriverLikeResponse handle() throws Exception {
    String sessionID = getSessionId();

    JSONObject caps = new JSONObject(getCalabashProxy().getSessionCapabilities(sessionID));

    JSONObject response = new JSONObject();
    response.put("sessionId", sessionID);
    response.put("status", 0);
    response.put("value", caps.toString());
    if (logger.isDebugEnabled()) {
      logger.debug("Session Capabilities for session: " + sessionID + " - ", response);
    }
    return new WebDriverLikeResponse(response);
  }
}
