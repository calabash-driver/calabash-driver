/*
 * Copyright 2012 calabash-driver committers.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package sh.calaba.driver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Capabilities of an Android testing device that can be weather a real device or an emulator.
 * 
 * @author ddary
 */
public class CalabashCapabilities {
  public static final String AUT = "aut";
  public static final String LOCALE = "locale";
  public static final String DEVICE_NAME = "deviceName";
  public static final String DEVICE_ID = "deviceId";
  public static final String MAX_INSTANCES = "maxInstances";
  public static final String NAME = "browserName";
  public static final String SDK_VERSION = "sdkVersion";
  public static final String ADDITIONAL_ADB_COMMANDS = "additionalAdbCommands";
  public static final String APP_BASE_PACKAGE = "appBasePackage";
  public static final String APP_MAIN_ACTIVITY = "appMainActivity";

  public static CalabashCapabilities android(String app, String sdkVersion, String locale,
      String deviceId, String deviceName, String appBasePackage, String appMainActivity) {
    CalabashCapabilities res = new CalabashCapabilities();
    res.setCapability(NAME, "calabash-android");
    res.setCapability(LOCALE, locale);
    res.setCapability(AUT, app);
    res.setCapability(SDK_VERSION, sdkVersion);
    res.setCapability(MAX_INSTANCES, 1);
    res.setCapability(DEVICE_ID, deviceId);
    res.setCapability(DEVICE_NAME, deviceName);
    res.setCapability(APP_BASE_PACKAGE, appBasePackage);
    res.setCapability(APP_MAIN_ACTIVITY, appMainActivity);
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
    calabashCapability.setAppBasePackage(capa.getString(CalabashCapabilities.APP_BASE_PACKAGE));
    calabashCapability.setAppMainActivity(capa.getString(CalabashCapabilities.APP_MAIN_ACTIVITY));

    if (capa.has(CalabashCapabilities.ADDITIONAL_ADB_COMMANDS)) {
      JSONArray commands = capa.getJSONArray(CalabashCapabilities.ADDITIONAL_ADB_COMMANDS);
      List<String> list = new ArrayList<String>();
      for (int i = 0; i < commands.length(); i++) {
        list.add(commands.getString(i));
      }
      calabashCapability.setAdditionalAdbCommands(list);
    }
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

  /**
   * @return ADB commands that will be executed before the calabash server will be started on the
   *         device.
   */
  public List<String> getAdditionalAdbCommands() {
    List<String> res = new ArrayList<String>();
    if (raw.get(ADDITIONAL_ADB_COMMANDS) != null) {
      res.addAll((Collection<String>) raw.get(ADDITIONAL_ADB_COMMANDS));
    }
    return res;
  }

  public String getMaxInstances() {
    Object o = raw.get(MAX_INSTANCES);
    return ((String) o);
  }

  public String getAppBasePackage() {
    Object o = raw.get(APP_BASE_PACKAGE);
    return ((String) o);
  }

  public String getAppMainActivity() {
    Object o = raw.get(APP_MAIN_ACTIVITY);
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

  public void setAppBasePackage(String appBasePackage) {
    raw.put(APP_BASE_PACKAGE, appBasePackage);
  }

  public void setAppMainActivity(String appMainActivity) {
    raw.put(APP_MAIN_ACTIVITY, appMainActivity);
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

  /**
   * @param commands The list of ADB commands to execute before the calabash server will be started
   *        on the device.
   */
  public void setAdditionalAdbCommands(List<String> commands) {
    raw.put(ADDITIONAL_ADB_COMMANDS, commands);
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
    result = prime * result + ((getAppBasePackage() == null) ? 0 : getAppBasePackage().hashCode());
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

    if (getAppBasePackage() == null) {
      if (other.getAppBasePackage() != null) return false;
    } else if (!getAppBasePackage().equals(other.getAppBasePackage())) return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Calabash Capabilities [raw=" + raw + ", getLocale()=" + getLocale()
        + ", getSDKVersion()=" + getSDKVersion() + ", getDeviceName()=" + getDeviceName()
        + ", getAppMainActivity()=" + getAppMainActivity() + ", getAppBasePackage()="
        + getAppBasePackage() + ", getDeviceId()=" + getDeviceId() + "]";
  }
}
