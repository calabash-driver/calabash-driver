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
package sh.calaba.driver.server.support;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import sh.calaba.driver.server.CalabashProxy;

public class CalabashInstrumentationServerStub extends NanoHTTPD {
  private CalabashTestSessionListener testSessionListener;

  public CalabashInstrumentationServerStub(int port) throws IOException {
    super(port, new File("."));
    System.out.println("CalabashInstrumentationServerStub is started on the following port: "
        + port);

  }

  public void registerTestSessionListener(CalabashTestSessionListener testSessionListener) {
    if (testSessionListener == null) {
      throw new IllegalArgumentException("The testSessionListener must not be null.");
    }
    if (this.testSessionListener != null) {
      throw new IllegalStateException(
          "Server does only support one listener and there is already one registered.");
    }
    this.testSessionListener = testSessionListener;
  }

  public Response serve(String uri, String method, Properties header, Properties params,
      Properties files) {
    // System.out.println("Hello world: " + uri);
    if (this.testSessionListener == null) {
      throw new IllegalStateException("Server must have one test session listener registered.");
    }
    if (uri.endsWith("/ping")) {
      return testSessionListener.ping(params);
    } else if (uri.endsWith("/kill")) {
      return testSessionListener.killServer(params);
    } else if (uri.endsWith("/ready")) {
      return testSessionListener.isServerReady(params);
    } else if (uri.endsWith("/screenshot")) {
      return testSessionListener.takeScreenshot(params);
    }

    return testSessionListener.executeCalabashCommand(params);
  }

  /**
   * To be implemented to listen to the {@link CalabashInstrumentationServerStub} lifecycle.
   * 
   * @author ddary
   * 
   */
  public abstract class CalabashTestSessionListener {
    public Response isServerReady(Properties params) {
      return defaultResponseWithMessage("true");
    }

    public Response killServer(Properties params) {
      return defaultResponseWithMessage("Affirmative!");
    }

    public Response ping(Properties params) {
      return defaultResponseWithMessage("pong");
    }

    @SuppressWarnings("deprecation")
    public Response takeScreenshot(Properties params) {
      ByteArrayInputStream stream = null;
      try {
        stream = new ByteArrayInputStream(IOUtils.toByteArray("Screenshot"));
      } catch (IOException e) {}

      return new NanoHTTPD.Response(HTTP_OK, "image/png", stream);
    }

    public abstract Response executeCalabashCommand(Properties params);

    protected Response defaultResponseWithMessage(String message) {
      return new Response(CalabashInstrumentationServerStub.HTTP_OK,
          CalabashInstrumentationServerStub.MIME_HTML, message);
    }

    protected Response defaultCalabashCommmandResponse() {
      return defaultResponseWithMessage("{\"bonusInformation\":[],\"message\":\"\",\"success\":true}");
    }
  }
}
