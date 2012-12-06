package sh.calaba.driver.server;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.server.support.CalabashTestServer;
import sh.calaba.driver.server.support.CapabilityFactory;

public class EndToEndTests {
  public static final String host = "localhost";

  @DataProvider(name = "testserver")
  public static Object[][] createTestServer() throws Exception {
    return new Object[][] {new Object[] {new CalabashTestServer()}};
  }

  @Test(dataProvider = "testserver")
  public void sessionIsInitializedAndCommand(CalabashTestServer server) throws Exception {
    RemoteCalabashAndroidDriver driver = null;
    try {
      CalabashCapabilities capa = CapabilityFactory.anAndroidCapability();
      driver =
          new RemoteCalabashAndroidDriver("http://" + host + ":" + server.getPort() + "/wd/hub",
              capa.getRawCapabilities());

      Assert.assertTrue(driver.getSession().getSessionId() != null,
          "Expectation is that a new session ID is created.");

      driver.findListItem(By.index(5)).press();

      driver.findTextField(By.text("username")).enterText("ddary_mobile");
      driver.findTextField(By.text("password")).enterText("password");
      driver.findButton(By.text("Sign In")).press();

    } finally {
      if (driver != null) {
        driver.quit();
        server.stopServer();
      }
    }
  }


}
