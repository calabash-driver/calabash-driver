package sh.calaba.driver.net;

import org.json.JSONException;
import org.json.JSONObject;

public class WebDriverLikeRequest {


  private String method;
  private String path;
  private JSONObject payload;

  public WebDriverLikeRequest(String method, Path path, JSONObject payload) {
    this.method = method;
    this.path = path.getPath();
    this.payload = payload;
  }

  public WebDriverLikeRequest(String method, String path, JSONObject payload) {
    this.method = method;
    this.path = path;
    this.payload = payload;
  }

  public boolean hasPayload() {
    return payload != null && payload.length() != 0;
  }

  public String toString() {
    String res = method + ":" + path;
    if (hasPayload()) {
      res += "\nbody:" + payload;
    }
    return res;
  }

  public String toJSON() throws JSONException {
    return toJSON(0);
  }


  public String toJSON(int i) throws JSONException {
    JSONObject o = new JSONObject();
    o.put("method", method);
    o.put("path", path);
    o.put("payload", payload);
    return o.toString(i);
  }



  public String getMethod() {
    return method;
  }



  public String getPath() {
    return path;
  }



  public JSONObject getPayload() {
    return payload;
  }

  public WebDriverLikeCommand getGenericCommand() {
    return WebDriverLikeCommand.getCommand(method, path);
  }

  public String getVariableValue(String variable) {
    WebDriverLikeCommand genericCommand = getGenericCommand();
    int i = genericCommand.getIndex(variable);
    String[] pieces = path.split("/");
    return pieces[i];
  }



}
