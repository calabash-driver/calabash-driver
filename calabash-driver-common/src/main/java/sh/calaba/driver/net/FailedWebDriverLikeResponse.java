/*
 * Copyright 2012 ios-driver committers.
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
package sh.calaba.driver.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sh.calaba.driver.exceptions.CalabashException;

public class FailedWebDriverLikeResponse extends WebDriverLikeResponse {

  public FailedWebDriverLikeResponse(JSONObject content) throws JSONException {
    super(content);
  }

  public FailedWebDriverLikeResponse(String sessionId, Exception e, String extraMessage) {
    // Webdriver Status 13 for UnknownError
    super(sessionId, 13, null);

    try {
      JSONObject value = new JSONObject();
      value.put("message", extraMessage + " , " + e.getMessage());
      value.put("class", e.getClass().getCanonicalName());

      JSONArray stacktace = new JSONArray();
      for (StackTraceElement el : e.getStackTrace()) {
        stacktace.put(el.toString());
      }
      value.put("stacktrace", stacktace);
      setValue(value);
    } catch (JSONException e1) {
      throw new CalabashException("Error creating JSONObject.", e1);
    }
  }

  public FailedWebDriverLikeResponse(String sessionId, Exception e) {
    this(sessionId, e, "");

  }
}
