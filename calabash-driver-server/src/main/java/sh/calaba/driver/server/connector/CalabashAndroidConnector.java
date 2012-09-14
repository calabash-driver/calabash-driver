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
package sh.calaba.driver.server.connector;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import sh.calaba.driver.CalabashCapabilities;


/**
 * Describes the connector that is used to communicate with the calabash http server that is running
 * on the device.
 * 
 * @author ddary
 * 
 */
public interface CalabashAndroidConnector {

  /**
   * @return The capabilities of the current session.
   */
  public abstract CalabashCapabilities getSessionCapabilities();

  /**
   * Sends the given JSON action command to the connected Calabash server on the device.
   * 
   * @param action The calabash action command to execute.
   * @return The result of the action based on the result of the Calabash server.
   * @throws IOException
   * @throws JSONException
   */
  public abstract JSONObject execute(JSONObject action) throws IOException, JSONException;

  /**
   * Cleans up the resources by closing the open connections.
   */
  public abstract void quit();

  /**
   * Method is actually starting this connector in connecting to the Calabash-server using a socket
   * connection. The connection is in the beginning verified by sending {@link #PING} command and
   * expecting the response {@link #PONG}.
   */
  public abstract void start();


  /**
   * Take screenshot.
   * 
   * @return the Screenshot
   */
  public JSONObject takeScreenshot();
}
