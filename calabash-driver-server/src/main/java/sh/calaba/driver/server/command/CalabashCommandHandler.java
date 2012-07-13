package sh.calaba.driver.server.command;

import org.json.JSONObject;

import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;
import sh.calaba.driver.server.CalabashProxy;

public class CalabashCommandHandler extends BaseCommandHandler {

  public CalabashCommandHandler(CalabashProxy calabashProxy, WebDriverLikeRequest request) {
    super(calabashProxy, request);
  }

  @Override
  public WebDriverLikeResponse handle() throws Exception {
    String sessionId = getSessionId();
    JSONObject response =
        getCalabashProxy().redirectMessageToCalabashServer(getRequest().getPayload(), sessionId);

    int responseStatusCode;
    if (response.getBoolean("success")) {
      responseStatusCode = 0;
    } else {
      responseStatusCode = 13;
    }

    return new WebDriverLikeResponse(sessionId, responseStatusCode, response);
  }

}
