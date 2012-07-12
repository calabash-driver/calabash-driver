package sh.calaba.driver.server.command.impl;

import org.json.JSONObject;

import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.command.BaseCommandHandler;

public class StopSession extends BaseCommandHandler {

  public StopSession(CalabashProxy proxy, WebDriverLikeRequest request) {
    super(proxy, request);
  }

  public WebDriverLikeResponse handle() throws Exception {
    String sessionId = getSessionId();
    // TODO validate that
    getCalabashProxy().stopCalabashConnector(sessionId);
    JSONObject o = new JSONObject();
    try {
      o.put("sessionId", sessionId);
      o.put("status", 0);
      o.put("value", "");
      WebDriverLikeResponse r = new WebDriverLikeResponse(o);
      return r;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    } finally {
      System.out.println("\n\n\n---------Session STOP ---------------\n\n\n");
    }

  }

}
