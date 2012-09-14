package sh.calaba.driver.server.support;

import java.util.ArrayList;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.server.CalabashNodeConfiguration;

/**
 * Configuration helper to setup the local calabash driver.
 * 
 * @author ddary
 * 
 */
public class CalabashLocalNodeConfiguration extends CalabashNodeConfiguration {
  public CalabashLocalNodeConfiguration(CalabashCapabilities capability,String driverHost, int driverPort) {
    super();
    this.capabilities = new ArrayList<CalabashCapabilities>();
    this.capabilities.add(capability);
    this.driverHost = driverHost;
    this.mobileAppPath = "notNeeded";
    this.mobileTestAppPath = "notNeeded";
    this.driverMaxSession = 1;
    this.driverPort = driverPort;
    this.driverRegistrationEnabled = false;
    this.installApksEnabled = false;
    this.cleanSavedUserDataEnabled=false;
  }
}
