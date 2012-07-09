package sh.calaba.driver.server.command;

import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.Handler;

public abstract class BaseCommandHandler implements Handler {

	private final WebDriverLikeRequest request;
	private final CalabashProxy calabashProxy;

	public BaseCommandHandler(CalabashProxy calabashProxy,
			WebDriverLikeRequest request) {
		this.request = request;
		this.calabashProxy = calabashProxy;
	}

	protected WebDriverLikeRequest getRequest() {
		return request;
	}

	protected CalabashProxy getCalabashProxy() {
		return calabashProxy;
	}
	
	protected String getSessionId(){
		return getRequest().getVariableValue(":sessionId");
	}
}
