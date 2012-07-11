package sh.calaba.driver.net;

import org.json.JSONObject;

import sh.calaba.driver.exceptions.CalabashException;
import sh.calaba.driver.model.ButtonSupport;
import sh.calaba.driver.model.L10nSupport;
import sh.calaba.driver.model.ListItemSupport;
import sh.calaba.driver.model.TextFieldSupport;
import sh.calaba.driver.model.ViewSupport;
import sh.calaba.driver.model.WaitingSupport;

public enum WebDriverLikeCommand {
	NEW_SESSION("POST", "/session", String.class), 
	GET_SESSION("GET","/session/:sessionId", JSONObject.class), 
    DELETE_SESSION("DELETE","/session/:sessionId", null), 
    GET_STATUS("GET","/status", JSONObject.class), 
    
    //Button
    BUTTON("GET","/session/:sessionId/button",ButtonSupport.class),
    TEXT_FIELD("POST","/session/:sessionId/textField",TextFieldSupport.class),
    VIEW("GET","/session/:sessionId/view",ViewSupport.class),
    L10N_SUPPORT("GET","/session/:sessionId/l10nSupport",L10nSupport.class),
    WAIT("POST","/session/:sessionId/wait",WaitingSupport.class),
    SCREENSHOT_WITH_NAME("POST","/session/:sessionId/screenshotWithName",ViewSupport.class),
    
	// LIST_ITEM
	LIST_ITEM("GET", "/session/:sessionId/listItem", ListItemSupport.class);

	private final String method;
	private final String path;
	private final Class<?> returnType;

	private WebDriverLikeCommand(String method, String path, Class<?> returnType) {
		this.method = method;
		this.path = path;
		this.returnType = returnType;
	}

	public String path() {
		return path;
	}

	public String method() {
		return method;
	}

	public Class<?> returnType() {
		return returnType;
	}

	public static WebDriverLikeCommand getCommand(String method, String path) {
		for (WebDriverLikeCommand command : values()) {
			if (command.isGenericFormOf(method, path)) {
				return command;
			}
		}
		throw new CalabashException("cannot find command for " + method + ", "
				+ path);
	}

	private boolean isGenericFormOf(String method, String path) {
		String genericPath = this.path;
		String genericMethod = this.method;
		if (!genericMethod.equals(method)) {
			return false;
		}
		String[] genericPieces = genericPath.split("/");
		String[] pieces = path.split("/");
		if (genericPieces.length != pieces.length) {
			return false;
		} else {
			for (int i = 0; i < pieces.length; i++) {
				String genericPiece = genericPieces[i];
				if (genericPiece.startsWith(":")) {
					continue;
				} else {
					if (!genericPiece.equals(pieces[i])) {
						return false;
					}
				}
			}
			return true;
		}
	}

	public int getIndex(String variable) {
		String[] pieces = path.split("/");
		for (int i = 0; i < pieces.length; i++) {
			String piece = pieces[i];
			if (piece.startsWith(":") && piece.equals(variable)) {
				return i;
			}
		}
		throw new CalabashException("cannot find the variable " + variable
				+ " in " + path);
	}

}
