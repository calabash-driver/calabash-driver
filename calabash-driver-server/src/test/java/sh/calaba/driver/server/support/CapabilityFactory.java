package sh.calaba.driver.server.support;

import sh.calaba.driver.CalabashCapabilities;

public class CapabilityFactory {
  public static final String ANY_VALUE = "ANY_VALUE";

  public static CalabashCapabilities anAndroidCapability() {
    return CalabashCapabilities.android(ANY_VALUE, ANY_VALUE, ANY_VALUE, ANY_VALUE, ANY_VALUE,
        ANY_VALUE);
  }
}
