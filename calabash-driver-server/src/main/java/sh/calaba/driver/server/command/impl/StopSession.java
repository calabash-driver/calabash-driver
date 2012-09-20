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
import sh.calaba.driver.server.connector.CalabashConnecterException;

public class StopSession extends BaseCommandHandler {
  final static Logger logger = LoggerFactory.getLogger(StopSession.class);

  public StopSession(CalabashProxy proxy, WebDriverLikeRequest request) {
    super(proxy, request);
  }

  public WebDriverLikeResponse handle() throws Exception {
    String sessionId = getSessionId();

    getCalabashProxy().stopCalabashConnector(sessionId);
    JSONObject responsePayload = new JSONObject();
    try {
      responsePayload.put("sessionId", sessionId);
      responsePayload.put("status", 0);
      responsePayload.put("value", "");
      WebDriverLikeResponse response = new WebDriverLikeResponse(responsePayload);
      return response;
    } catch (Exception e) {
      throw new CalabashConnecterException("Error occured while ending the session", e);
    } finally {
      if (logger.isDebugEnabled()) {
        logger.debug("\n\n\n---------Session STOP ---------------\n\n\n");
      }
    }
  }
}
