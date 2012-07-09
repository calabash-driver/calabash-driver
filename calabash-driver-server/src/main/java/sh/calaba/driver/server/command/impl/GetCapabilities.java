package sh.calaba.driver.server.command.impl;

import org.json.JSONObject;

import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.command.BaseCommandHandler;

public class GetCapabilities extends BaseCommandHandler {
	public GetCapabilities(CalabashProxy proxy, WebDriverLikeRequest request) {
		super(proxy, request);

	}

	public WebDriverLikeResponse handle() throws Exception {
		String sessionID = getSessionId();

		JSONObject caps = new JSONObject(getCalabashProxy()
				.getSessionCapabilities(sessionID));

		JSONObject response = new JSONObject();
		response.put("sessionId", sessionID);
		response.put("status", 0);
		response.put("value", caps.toString());
		return new WebDriverLikeResponse(response);
	}
}
