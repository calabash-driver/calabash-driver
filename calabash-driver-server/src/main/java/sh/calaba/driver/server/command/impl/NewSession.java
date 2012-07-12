package sh.calaba.driver.server.command.impl;

import org.json.JSONObject;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.command.BaseCommandHandler;

public class NewSession extends BaseCommandHandler {

  public NewSession(CalabashProxy proxy, WebDriverLikeRequest request) {
    super(proxy, request);
  }

  public WebDriverLikeResponse handle() throws Exception {
    JSONObject desiredCapabilities = getRequest().getPayload().getJSONObject("desiredCapabilities");
    System.out.println("desiredCapabilities: " + desiredCapabilities);
    CalabashCapabilities capabilities = CalabashCapabilities.fromJSON(desiredCapabilities);

    JSONObject json = new JSONObject();
    String sessionID = getCalabashProxy().initializeSessionForCapabilities(capabilities);
    System.out.println("New Session Class; session iD: " + sessionID);
    json.put("sessionId", sessionID);
    json.put("status", 0);
    json.put("value", "/DOMINIK/PLEASE/FIX/THIS");
    WebDriverLikeResponse r = new WebDriverLikeResponse(json);

    System.out.println("Created session ID in response: " + r.getSessionId());
    return r;
  }
}
