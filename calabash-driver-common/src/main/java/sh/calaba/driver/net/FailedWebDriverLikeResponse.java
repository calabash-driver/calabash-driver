package sh.calaba.driver.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FailedWebDriverLikeResponse extends WebDriverLikeResponse {

  public FailedWebDriverLikeResponse(JSONObject content) throws JSONException {
    super(content);
  }

  public FailedWebDriverLikeResponse(String sessionId, Exception e, String extraMessage) {
    super(sessionId, 13, null);

    try {
      JSONObject value = new JSONObject();
      value.put("message", extraMessage + " , " + e.getMessage());
      value.put("class", e.getClass().getCanonicalName());
      // TODO freynaud
      value.put("screen", "TODO");

      JSONArray stacktace = new JSONArray();
      for (StackTraceElement el : e.getStackTrace()) {
        stacktace.put(el.toString());
      }
      value.put("stacktrace", stacktace);
      setValue(value);
    } catch (JSONException e1) {
      // TODO freynaud
      throw new RuntimeException("NI");
    }
  }

  public FailedWebDriverLikeResponse(String sessionId, Exception e) {
    this(sessionId, e, "");

  }

}
