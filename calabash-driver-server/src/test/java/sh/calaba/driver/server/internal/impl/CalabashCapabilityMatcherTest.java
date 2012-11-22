package sh.calaba.driver.server.internal.impl;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.server.internal.CapabilityMatcher;

public class CalabashCapabilityMatcherTest {
  public static final String APP = "demo:123";
  public static final String SDK_VERSION = "4.1";
  public static final String LOCALE = "uk_UK";
  public static final String DEVICE_NAME = "emulator";

  private CalabashCapabilities nodeCapability = null;
  private CapabilityMatcher matcher = null;

  @Test
  public void matchMinimumCapa() {
    CalabashCapabilities reqCapa = new CalabashCapabilities();
    reqCapa.setAut(APP);
    reqCapa.setLocale(LOCALE);
    Assert.assertTrue(matcher.matches(nodeCapability.getRawCapabilities(),
        reqCapa.getRawCapabilities()));
  }

  @Test
  public void onlyDefaultCapabilitiesAreConsidered() {
    CalabashCapabilities reqCapa = new CalabashCapabilities();
    reqCapa.setAut(APP);
    reqCapa.setLocale(LOCALE);
    reqCapa.setSDKVersion(SDK_VERSION);
    reqCapa.setDeviceId(APP);
    Assert.assertTrue(matcher.matches(nodeCapability.getRawCapabilities(),
        reqCapa.getRawCapabilities()));
  }
  
  
  @Test
  public void matchConsideredCapa() {
    CalabashCapabilities reqCapa = new CalabashCapabilities();
    reqCapa.setAut(APP);
    reqCapa.setLocale(LOCALE);
    reqCapa.setSDKVersion(SDK_VERSION);
    Assert.assertTrue(matcher.matches(nodeCapability.getRawCapabilities(),
        reqCapa.getRawCapabilities()));
  }
  @Test
  public void doesntMatchConsideredCapa() {
    CalabashCapabilities reqCapa = new CalabashCapabilities();
    reqCapa.setAut(APP);
    reqCapa.setLocale(LOCALE);
    reqCapa.setSDKVersion(DEVICE_NAME);
    Assert.assertFalse(matcher.matches(nodeCapability.getRawCapabilities(),
        reqCapa.getRawCapabilities()));
    
    reqCapa.setAut(APP);
    reqCapa.setLocale(DEVICE_NAME);
    reqCapa.setSDKVersion(SDK_VERSION);
    Assert.assertFalse(matcher.matches(nodeCapability.getRawCapabilities(),
        reqCapa.getRawCapabilities()));
  }

  @Test
  public void doesntMatchMinimumCapa() {
    CalabashCapabilities reqCapa = new CalabashCapabilities();
    reqCapa.setAut(APP);
    reqCapa.setLocale(DEVICE_NAME);
    Assert.assertFalse(matcher.matches(nodeCapability.getRawCapabilities(),
        reqCapa.getRawCapabilities()));
    reqCapa = new CalabashCapabilities();
    reqCapa.setAut(DEVICE_NAME);
    reqCapa.setLocale(LOCALE);
    Assert.assertFalse(matcher.matches(nodeCapability.getRawCapabilities(),
        reqCapa.getRawCapabilities()));
  }

  @BeforeTest
  public void setup() {
    this.matcher = new CalabashCapabilityMatcher();
    this.nodeCapability =
        CalabashCapabilities.android(APP, SDK_VERSION, LOCALE, "deviceId", DEVICE_NAME,
            "appBasePackage", "appMainActivity");
  }
}
