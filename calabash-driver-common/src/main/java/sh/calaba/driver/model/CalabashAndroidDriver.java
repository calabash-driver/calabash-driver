/*
 * Copyright 2012 ios-driver committers.
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
package sh.calaba.driver.model;

import java.net.URL;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONObject;

import sh.calaba.driver.exceptions.CalabashException;
import sh.calaba.driver.net.Helper;
import sh.calaba.driver.net.HttpClientFactory;
import sh.calaba.driver.net.Session;
import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;

/**
 * Client driver for Http communication handling.
 */
public abstract class CalabashAndroidDriver {

  private final String remoteURL;
  private final Map<String, Object> requestedCapabilities;
  private final Session session;
  private static String host;
  private static int port;

  public CalabashAndroidDriver(String remoteURL, Map<String, Object> capabilities) {
    this.remoteURL = remoteURL;
    this.requestedCapabilities = capabilities;
    try {
      URL url = new URL(remoteURL);
      port = url.getPort();
      host = url.getHost();
      session = start();
    } catch (Exception e) {
      e.printStackTrace();

      throw new RuntimeException(e);
    }
  }

  private Session start() throws Exception {
    JSONObject payload = new JSONObject();
    payload.put("desiredCapabilities", requestedCapabilities);
    WebDriverLikeRequest request = new WebDriverLikeRequest("POST", "/session", payload);
    WebDriverLikeResponse response = execute(request);
    String sessionId = response.getSessionId();

    Session session = new Session(sessionId);
    return session;
  }

  public WebDriverLikeResponse execute(WebDriverLikeRequest request) throws Exception {
    HttpClient client = HttpClientFactory.getClient();

    String url = remoteURL + request.getPath();
    BasicHttpEntityEnclosingRequest r =
        new BasicHttpEntityEnclosingRequest(request.getMethod(), url);
    if (request.hasPayload()) {
      r.setEntity(new StringEntity(request.getPayload().toString(), "UTF-8"));
    }

    HttpHost h = new HttpHost(host, port);

    HttpResponse response = client.execute(h, r);
    if (response.getStatusLine().getStatusCode() == 500) {
      throw new CalabashException(
          "The Server responded with a server error. Is the Calabash-driver-server started?",
          new Throwable(response.toString()));
    }

    JSONObject o = Helper.extractObject(response);

    return new WebDriverLikeResponse(o);
  }

  public Session getSession() {
    return session;
  }

  public void quit() {
    WebDriverLikeRequest request =
        new WebDriverLikeRequest("DELETE", "/session/" + session.getSessionId(), new JSONObject());
    try {
      execute(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
