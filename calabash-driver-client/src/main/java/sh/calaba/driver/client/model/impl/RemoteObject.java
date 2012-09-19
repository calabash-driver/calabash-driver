/*
 * Copyright 2012 ios-driver committers. Copyright 2012 calabash-driver committers.
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

import org.json.JSONException;
import org.json.JSONObject;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.exceptions.CalabashException;
import sh.calaba.driver.net.Path;
import sh.calaba.driver.net.Session;
import sh.calaba.driver.net.WebDriverLikeCommand;
import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;

public class RemoteObject {

  private final RemoteCalabashAndroidDriver driver;

  public RemoteObject(RemoteCalabashAndroidDriver driver) {
    this.driver = driver;
  }

  public JSONObject executeCalabashCommand(CalabashCommands calabashCommand, String... parameter) {
    try {
      JSONObject payload = new JSONObject();
      payload.put("command", calabashCommand.getCommand());
      payload.put("arguments", parameter);

      Object res = get(calabashCommand.getWebDriverLikeCommand(), payload);
      JSONObject result = ((JSONObject) res);

      Boolean status = (Boolean) result.get("success");
      if (result == null || !status) {
        throw new CalabashException("Calabash command '" + calabashCommand.getCommand()
            + "' was not successful: " + result);
      }
      return result;
    } catch (JSONException e) {
      e.printStackTrace();
      throw new CalabashException("Error ocured while parsing calabash-driver response.", e);
    }
  }

  protected Object get(WebDriverLikeCommand command, JSONObject payload) {
    Path p = new Path(command).withSession(getSessionId());

    WebDriverLikeRequest request = new WebDriverLikeRequest(command.method(), p, payload);
    WebDriverLikeResponse response = execute(request);
    if (response == null || response.getValue() == JSONObject.NULL) {
      return null;
    } else {
      return response.getValue();
    }
  }

  public WebDriverLikeResponse execute(WebDriverLikeRequest request) {
    try {
      return driver.execute(request);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  protected RemoteCalabashAndroidDriver getDriver() {
    return driver;
  }

  public Session getSession() {
    return driver.getSession();
  }

  public String getSessionId() {
    return getSession().getSessionId();
  }

  @Override
  public String toString() {
    return getClass().getName();
  }
}
