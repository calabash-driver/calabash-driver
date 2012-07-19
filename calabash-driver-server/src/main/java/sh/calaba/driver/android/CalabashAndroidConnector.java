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
package sh.calaba.driver.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import sh.calaba.driver.CalabashCapabilities;

/**
 * The Calabash Android Client Driver.
 * 
 * @author ddary
 * 
 */
public class CalabashAndroidConnector {
  private Socket calabashClientSocket = null;
  private BufferedReader in = null;
  private PrintWriter out = null;
  public static final String PING = "Ping!";
  public static final String PONG = "Pong!";
  private String hostname;
  private int port;
  private CalabashCapabilities sessionCapabilities;

  public CalabashAndroidConnector(String hostname, int port,
      CalabashCapabilities sessionCapabilities) {
    this.hostname = hostname;
    this.port = port;
    this.sessionCapabilities = sessionCapabilities;
  }

  public CalabashCapabilities getSessionCapabilities() {
    return sessionCapabilities;
  }

  public JSONObject execute(JSONObject action) throws IOException, JSONException {

    // Sending operation to the server
    out.println(action);

    // Reading response
    String responseString = in.readLine();
    // System.out.println("JSON response: " + responseString);

    return new JSONObject(responseString);
  }

  protected void finalize() throws Throwable {
    try {
      quit(); // close open files
    } finally {
      super.finalize();
    }
  }

  public void quit() {
    if (!calabashClientSocket.isClosed()) {
      try {
        out.close();
        in.close();
        calabashClientSocket.close();
      } catch (IOException e) {
        throw new RuntimeException("Unable to close the socket to calabash client: ", e);
      }
    }
  }

  /**
   * @see #initializeCalabashServer()
   */
  public void startConnector() {
    try {

      if (calabashClientSocket == null) {
        System.out.println(String.format("Connecting to calabash server %s on port %s", hostname,
            port));
        calabashClientSocket = new Socket(hostname, port);
      }
      out = new PrintWriter(calabashClientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(calabashClientSocket.getInputStream()));
      out.println(PING);
      String response = in.readLine();
      System.out.println(PING + "request - Response: " + response);
      if (!PONG.equals(response)) {
        throw new RuntimeException("Calabash server is not responding as expected (PONG): "
            + response);
      }
    } catch (UnknownHostException e) {
      throw new RuntimeException("Don't know about host: " + hostname);
    } catch (IOException e) {
      throw new RuntimeException("Couldn't get I/O for " + "the connection to: " + hostname, e);
    }
  }
}
