package sh.calaba.driver;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class CalabashCapabilities {

  public static final String AUT = "aut";
  public static final String LOCALE = "locale";
  public static final String DEVICE_NAME = "deviceName";
  public static final String DEVICE_ID = "deviceId";
  public static final String MAX_INSTANCES = "maxInstances";
  public static final String NAME = "browserName";
  public static final String SDK_VERSION = "sdkVersion";

  public static CalabashCapabilities android(String app, String sdkVersion, String locale,
      String deviceId, String deviceName) {
    CalabashCapabilities res = new CalabashCapabilities();
    res.setCapability(NAME, "calabash-android");
    res.setCapability(LOCALE, locale);
    res.setCapability(AUT, app);
    res.setCapability(SDK_VERSION, sdkVersion);
    res.setCapability(MAX_INSTANCES, 1);
    res.setCapability(DEVICE_ID, deviceId);
    res.setCapability(DEVICE_NAME, deviceName);
    return res;
  }

  public static CalabashCapabilities fromJSON(JSONObject capa) throws JSONException {
    CalabashCapabilities calabashCapability = new CalabashCapabilities();
    calabashCapability.setDeviceName(capa.getString(CalabashCapabilities.DEVICE_NAME));
    calabashCapability.setName(capa.getString(CalabashCapabilities.NAME));
    calabashCapability.setMaxInstances(capa.getInt(CalabashCapabilities.MAX_INSTANCES));
    calabashCapability.setLocale(capa.getString(CalabashCapabilities.LOCALE));

    calabashCapability.setDeviceId(capa.getString(CalabashCapabilities.DEVICE_ID));
    calabashCapability.setSDKVersion(capa.getString(CalabashCapabilities.SDK_VERSION));
    calabashCapability.setAut(capa.getString(CalabashCapabilities.AUT));
    return calabashCapability;
  }

  private final Map<String, Object> raw = new HashMap<String, Object>();

  public CalabashCapabilities() {}

  public CalabashCapabilities(Map<String, Object> from) {
    raw.putAll(from);
  }

  public String getApplication() {
    Object o = raw.get(AUT);
    return ((String) o);
  }

  public String getLocale() {
    Object o = raw.get(LOCALE);
    return ((String) o);
  }

  public String getMaxInstances() {
    Object o = raw.get(MAX_INSTANCES);
    return ((String) o);
  }

  public Map<String, Object> getRawCapabilities() {
    return raw;
  }

  public String getSDKVersion() {
    Object o = raw.get(SDK_VERSION);
    return ((String) o);
  }

  public void setCapability(String key, Object value) {
    raw.put(key, value);
  }

  public void setLocale(String locale) {
    raw.put(LOCALE, locale);

  }

  public void setMaxInstances(Integer maxInstances) {
    raw.put(MAX_INSTANCES, maxInstances);
  }

  public void setSDKVersion(String sdkVersion) {
    raw.put(SDK_VERSION, sdkVersion);
  }

  public String getDeviceName() {
    Object o = raw.get(DEVICE_NAME);
    return ((String) o);
  }

  public void setDeviceName(String deviceName) {
    raw.put(DEVICE_NAME, deviceName);
  }

  public String getDeviceId() {
    Object o = raw.get(DEVICE_ID);
    return ((String) o);
  }

  public void setDeviceId(String deviceId) {
    raw.put(DEVICE_ID, deviceId);
  }

  public void setName(String name) {
    raw.put(NAME, name);
  }

  public void setAut(String aut) {
    raw.put(AUT, aut);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
    result = prime * result + ((getLocale() == null) ? 0 : getLocale().hashCode());
    result = prime * result + ((getSDKVersion() == null) ? 0 : getSDKVersion().hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    CalabashCapabilities other = (CalabashCapabilities) obj;
    if (getDeviceId() == null) {
      if (other.getDeviceId() != null) return false;
    } else if (!getDeviceId().equals(other.getDeviceId())) return false;
    if (getLocale() == null) {
      if (other.getLocale() != null) return false;
    } else if (!getLocale().equals(other.getLocale())) return false;
    if (getSDKVersion() == null) {
      if (other.getSDKVersion() != null) return false;
    } else if (!getSDKVersion().equals(other.getSDKVersion())) return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "CalabashCapabilities [raw=" + raw + ", getLocale()=" + getLocale()
        + ", getSDKVersion()=" + getSDKVersion() + ", getDeviceName()=" + getDeviceName()
        + ", getDeviceId()=" + getDeviceId() + "]";
  }

  public static CalabashCapabilities nexus() {
    return android("eBay:1.7.0", "4.0.4", "de_DE", "0149948604012003", "GalaxyNexus");
  }

}
