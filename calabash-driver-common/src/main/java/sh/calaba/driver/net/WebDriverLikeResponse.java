package sh.calaba.driver.net;

import org.json.JSONException;
import org.json.JSONObject;

public class WebDriverLikeResponse {

  private String sessionId;
  private int status;
  private Object value;

  protected WebDriverLikeResponse() {
  }
  
  public WebDriverLikeResponse(String sessionId,int status,Object value){
    this.sessionId = sessionId;
    this.status = status;
    this.value = value;
  }
  public WebDriverLikeResponse(JSONObject content) throws JSONException {
    this.sessionId = content.getString("sessionId");
    this.status = content.getInt("status");
    this.value = content.get("value");
  }

  
  public String getSessionId() {
    return sessionId;
  }

  public int getStatus() {
    return status;
  }

  public Object getValue() {
    return value;
  }
  
  public String stringify() throws JSONException{
    JSONObject o = new JSONObject();
    o.put("sessionId", sessionId);
    o.put("status",status);
    o.put("value", value);
    return o.toString(2);
  }
  
  protected void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  protected void setStatus(int status) {
    this.status = status;
  }
  protected void setValue(Object value) {
    this.value = value;
  }
  
  

}
