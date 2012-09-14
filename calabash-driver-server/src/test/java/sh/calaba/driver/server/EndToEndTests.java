package sh.calaba.driver.server;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.server.support.CalabashLocalNodeConfiguration;
import sh.calaba.driver.server.support.CapabilityFactory;

public class EndToEndTests {

  private CalabashAndroidServer server;
  private String host = "127.0.0.1";
  private int port = 4444;

  @BeforeClass
  public void startServer() throws Exception {
    CalabashLocalNodeConfiguration conf =
        new CalabashLocalNodeConfiguration(CapabilityFactory.anAndroidCapability(), host, port);
    server = new CalabashAndroidServer(conf);
    server.start();
  }

  @Test(enabled = true)
  public void scriptStartsAndRegisterToServer() {
    RemoteCalabashAndroidDriver driver = null;
    try {
      CalabashCapabilities capa = CapabilityFactory.anAndroidCapability();
      driver = new RemoteCalabashAndroidDriver("http://" + host + ":" + port + "/wd/hub", capa.getRawCapabilities());

      System.out.println("created session: " + driver.getSession().getSessionId());
      System.out.println("session cap: " + driver.getSession().getActualCapabilities());

      driver.listItem(By.index(5)).press();

      driver.textField(By.text("username")).enterText("ddary_mobile");
      driver.textField(By.text("password")).enterText("password");
      driver.button(By.text("Sign In")).press();
      /*
       * RemoteUIATarget target = driver.getLocalTarget();
       * Assert.assertEquals(target.getReference(), "1"); target = (RemoteUIATarget)
       * driver.getLocalTarget(); Assert.assertEquals(target.getReference(), "2");
       */
    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }

  @AfterClass
  public void stopServer() throws Exception {
    server.stop();
  }
}
