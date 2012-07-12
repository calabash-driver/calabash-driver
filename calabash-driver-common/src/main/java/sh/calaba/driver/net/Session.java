package sh.calaba.driver.net;

import java.util.Map;
import java.util.UUID;

public class Session {
  private final String opaqueKey;
  private final Map<String, Object> desiredCapabilities;
  private Map<String, Object> actualCapabilities;

  public static Session createSession(Map<String, Object> desiredCapabilities) {
    String key = UUID.randomUUID().toString();
    Session res = new Session(key, desiredCapabilities);
    return res;
  }

  public Session(String key) {
    this.opaqueKey = key;
    this.desiredCapabilities = null;
  }

  @SuppressWarnings("unused")
  private Session() {
    throw new IllegalAccessError();
  }

  private Session(String key, Map<String, Object> desiredCapabilities) {
    this.opaqueKey = key;
    this.desiredCapabilities = desiredCapabilities;
  }

  public String getSessionId() {
    return opaqueKey;
  }

  public Map<String, Object> getDesiredCapabilities() {
    return desiredCapabilities;
  }

  public void setActualCapabilities(Map<String, Object> capabilities) {
    this.actualCapabilities = capabilities;
  }

  public Map<String, Object> getActualCapabilities() {
    return actualCapabilities;
  }
}
