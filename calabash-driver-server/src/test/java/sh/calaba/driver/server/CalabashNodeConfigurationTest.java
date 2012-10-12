package sh.calaba.driver.server;

import java.io.File;
import java.net.URI;
import java.security.InvalidParameterException;

import org.testng.Assert;
import org.testng.annotations.Test;

import sh.calaba.driver.server.exceptions.CalabashConfigurationException;
import sh.calaba.driver.server.support.CalabashWebNodeConfigStub;

public class CalabashNodeConfigurationTest {
  private static final String CONFIG_FILE_URL = "http://localhost:8080/config.json";
  private static final String CONFIG_FILE = "src/test/resources/nodeconfig/config.json";

  @Test
  public void testReadConfigByFile() throws Exception {
    CalabashNodeConfiguration config =
        CalabashNodeConfiguration.readFromFile(new File(CONFIG_FILE));
    assertNodeConfig(config);
  }

  @Test
  public void testReadConfigByURI() throws Exception {
    CalabashWebNodeConfigStub stub = new CalabashWebNodeConfigStub();
    Thread.sleep(3000);
    CalabashNodeConfiguration config =
        CalabashNodeConfiguration.readFromURI(new URI(CONFIG_FILE_URL));
    assertNodeConfig(config);

    stub.stop();
  }

  private void assertNodeConfig(CalabashNodeConfiguration config) throws Exception {
    Assert.assertTrue(config.isDriverRegistrationEnabled());
    Assert.assertEquals(config.getMobileAppPath(), "DriverDemo_1.apk");
    Assert.assertEquals(config.getMobileTestAppPath(), "Test.apk");
    Assert.assertFalse(config.isInstallApksEnabled());
    Assert.assertTrue(config.isCleanSavedUserDataEnabled());
    Assert.assertEquals(config.getDriverPort(), 4445);
    Assert.assertEquals(config.getDriverHost(), "localhost");
    Assert.assertEquals(config.getHubHost(), "localhost");
    Assert.assertEquals(config.getHubPort(), 4444);
    Assert.assertEquals(config.getProxy(), "org.openqa.calabash.CalabashSessionProxy");
  }

  @Test(expectedExceptions = {CalabashConfigurationException.class})
  public void testReadConfigByFileInvalidFilePath() throws Exception {
    CalabashNodeConfiguration.readFromFile(new File(""));
  }

  @Test(expectedExceptions = {InvalidParameterException.class})
  public void testReadConfigByFileNullInput() throws Exception {
    CalabashNodeConfiguration.readFromFile(null);
  }

  @Test(expectedExceptions = {InvalidParameterException.class})
  public void testReadConfigByURIInvalidURI() throws Exception {
    CalabashNodeConfiguration.readFromURI(new URI(""));
  }

  @Test(expectedExceptions = {InvalidParameterException.class})
  public void testReadConfigByURINullInput() throws Exception {
    CalabashNodeConfiguration.readFromURI(null);
  }
}
