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
package sh.calaba.driver.server.connector.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.server.connector.CalabashAndroidConnector;
import sh.calaba.driver.server.connector.CalabashConnecterException;

/**
 * The Calabash Android Client that is connecting to the Calabash-Android server that is running on
 * the device.
 * 
 * @author ddary
 */
public class CalabashAndroidConnectorImpl implements CalabashAndroidConnector {
  private DefaultHttpClient httpClient;
  private String hostname;
  private int port;
  private CalabashCapabilities sessionCapabilities;

  public CalabashAndroidConnectorImpl(String hostname, int port,
      CalabashCapabilities sessionCapabilities) {
    this.hostname = hostname;
    this.port = port;
    this.sessionCapabilities = sessionCapabilities;
  }

  /*
   * (non-Javadoc)
   * 
   * @see sh.calaba.driver.android.CalabashAndroidConnectorI#getSessionCapabilities()
   */
  @Override
  public CalabashCapabilities getSessionCapabilities() {
    return sessionCapabilities;
  }

  /*
   * (non-Javadoc)
   * 
   * @see sh.calaba.driver.android.CalabashAndroidConnectorI#execute(String, org.json.JSONObject)
   */
  @Override
  public JSONObject execute(JSONObject action) throws IOException, JSONException {
    return new JSONObject(extractStringResponse(execute("/", action)));
  }

  /*
   * (non-Javadoc)
   * 
   * @see sh.calaba.driver.server.connector.CalabashAndroidConnector#takeScreenshot()
   */
  public JSONObject takeScreenshot() {
    HttpResponse screenshot = null;
    JSONObject result = new JSONObject();

    try {
     
      screenshot = execute("/screenshot", null);

      List<String> extras = new ArrayList<String>();
      byte[] byteArray = IOUtils.toByteArray(screenshot.getEntity().getContent());
      extras.add(Base64.encodeBase64String(byteArray));
      result.put("success", true);
      result.put("bonusInformation", extras);
      result.put("message", "");

    } catch (JSONException e) {
      throw new CalabashConnecterException(e);
    } catch (IllegalStateException e) {
      throw new CalabashConnecterException(e);
    } catch (IOException e) {
      throw new CalabashConnecterException(e);
    }

    return result;

  }

  private HttpResponse execute(String path, JSONObject action) {
    HttpResponse response = null;
    try {
      HttpPost postRequest = new HttpPost("http://" + hostname + ":" + port + path);
      postRequest.addHeader("Content-Type", "application/json;charset=utf-8");
      if (action != null) {
        postRequest.setEntity(new StringEntity(action.toString(), "UTF-8"));
      }

      response = httpClient.execute(postRequest);
    } catch (UnsupportedEncodingException e) {
      throw new CalabashConnecterException(e);
    } catch (ClientProtocolException e) {
      throw new CalabashConnecterException(e);
    } catch (IOException e) {
      throw new CalabashConnecterException(e);
    }
    if (response.getStatusLine().getStatusCode() != 200) {
      throw new RuntimeException("Failed : HTTP error code : "
          + response.getStatusLine().getStatusCode());
    }
    return response;
  }

  private String extractStringResponse(HttpResponse response) {
    StringWriter writer = new StringWriter();
    try {
      IOUtils.copy(response.getEntity().getContent(), writer);
    } catch (IllegalStateException e) {
      throw new CalabashConnecterException(e);
    } catch (IOException e) {
      throw new CalabashConnecterException(e);
    }
    String responseString = writer.toString();

    return responseString;
  }

  /**
   * Cleans up the connector using the {@link #quit()} method.
   */
  protected void finalize() throws Throwable {
    try {
      quit(); // close open files
    } finally {
      super.finalize();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see sh.calaba.driver.android.CalabashAndroidConnectorI#quit()
   */
  @Override
  public void quit() {
    execute("/kill", null);
    httpClient.getConnectionManager().shutdown();
  }

  /*
   * (non-Javadoc)
   * 
   * @see sh.calaba.driver.android.CalabashAndroidConnectorI#startConnector()
   */
  @Override
  public void start() {
    System.out.println("Starting Calabash HTTP Connector");
    httpClient = new DefaultHttpClient();
    this.new CalabashHttpServerWaiter().run();
  }

  public class CalabashHttpServerWaiter implements Runnable {
    private Lock lock = new ReentrantLock();
    private Condition cv = lock.newCondition();

    private boolean isPortBound() {
      Boolean ready = Boolean.parseBoolean(extractStringResponse(execute("/ready", null)));

      if (ready == null || ready == Boolean.FALSE) {
        return Boolean.FALSE;
      } else {
        return Boolean.TRUE;
      }
    }



    @Override
    public void run() {
      lock.lock();

      try {
        Boolean portIsNotBound = !isPortBound();

        while (portIsNotBound) {
          cv.await(2, TimeUnit.SECONDS);
          portIsNotBound = !isPortBound();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }

}
