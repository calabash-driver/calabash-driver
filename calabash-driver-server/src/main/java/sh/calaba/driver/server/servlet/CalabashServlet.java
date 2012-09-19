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
package sh.calaba.driver.server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.calaba.driver.net.FailedWebDriverLikeResponse;
import sh.calaba.driver.net.WebDriverLikeCommand;
import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;
import sh.calaba.driver.server.Handler;
import sh.calaba.driver.server.command.CommandMapping;

public class CalabashServlet extends CalabashProxyBasedServlet {
  final Logger logger = LoggerFactory.getLogger(CalabashProxyBasedServlet.class);
  private static final long serialVersionUID = -8544875030463578977L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      process(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      process(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      process(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
    WebDriverLikeRequest req = getRequest(request);
    WebDriverLikeResponse resp = getResponse(req);

    response.setContentType("application/json;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");

    if (req.getGenericCommand() == WebDriverLikeCommand.NEW_SESSION) {
      String session = resp.getSessionId();
      if (logger.isDebugEnabled()) {
        logger.debug("CalabashServlet: new session: " + session);
      }
      String scheme = request.getScheme(); // http
      String serverName = request.getServerName(); // hostname.com
      int serverPort = request.getServerPort(); // 80
      String contextPath = request.getContextPath(); // /mywebapp

      // Reconstruct original requesting URL
      String url = scheme + "://" + serverName + ":" + serverPort + contextPath;
      response.setHeader("Location", url + "/session/" + session);
      response.setStatus(301);
      writeResultToResponse(response, resp);
    } else {
      response.setStatus(200);
      writeResultToResponse(response, resp);
    }
  }

  private void writeResultToResponse(HttpServletResponse response, WebDriverLikeResponse resp)
      throws Exception {
    String responseTXT = resp.stringify();

    response.getWriter().print(responseTXT);
    response.getWriter().close();
  }

  private WebDriverLikeRequest getRequest(HttpServletRequest request) throws Exception {
    String method = request.getMethod();
    String path = request.getPathInfo();
    String json = null;
    if (request.getInputStream() != null) {
      BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
      StringBuilder s = new StringBuilder();
      String line;
      while ((line = rd.readLine()) != null) {
        s.append(line);
      }
      rd.close();
      json = s.toString();
    }
    JSONObject o = new JSONObject();
    if (json != null && !json.isEmpty()) {
      o = new JSONObject(json);
    }

    WebDriverLikeRequest orig = new WebDriverLikeRequest(method, path, o);
    return orig;

  }

  private WebDriverLikeResponse getResponse(WebDriverLikeRequest request) {
    WebDriverLikeCommand wdlc = request.getGenericCommand();
    try {

      Handler h = CommandMapping.get(wdlc).createHandler(getCalabashProxy(), request);
      return h.handle();
    } catch (Exception e) {
      logger.error("Error occured: " + request.getPath() + " Error: " + e);
      logger.error(request.toString());

      return new FailedWebDriverLikeResponse(request.getVariableValue(":sessionId"), e);
    }
  }
}
