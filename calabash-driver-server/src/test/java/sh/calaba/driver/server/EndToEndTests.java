package sh.calaba.driver.server;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.server.support.AdbConnectionStub;
import sh.calaba.driver.server.support.CalabashLocalNodeConfiguration;
import sh.calaba.driver.server.support.CapabilityFactory;
import sh.calaba.driver.utils.CalabashAdbCmdRunner;

public class EndToEndTests {

  private CalabashAndroidServer server;
  private String host = "127.0.0.1";
  private int port = 4444;

  @BeforeClass
  public void startServer() throws Exception {
    CalabashLocalNodeConfiguration conf =
        new CalabashLocalNodeConfiguration(CapabilityFactory.anAndroidCapability(), host, port);
    CalabashProxy proxy = new CalabashProxy();
    proxy.setCalabashAdbCmdRunner(new CalabashAdbCmdRunner(new AdbConnectionStub()));
    server = new CalabashAndroidServer();
    server.setProxy(proxy);
    server.start(conf);
  }

  @Test(enabled = true)
  public void scriptStartsAndRegisterToServer() {
    RemoteCalabashAndroidDriver driver = null;
    try {
      CalabashCapabilities capa = CapabilityFactory.anAndroidCapability();
      driver =
          new RemoteCalabashAndroidDriver("http://" + host + ":" + port + "/wd/hub",
              capa.getRawCapabilities());

      System.out.println("created session: " + driver.getSession().getSessionId());
      System.out.println("session cap: " + driver.getSession().getActualCapabilities());

      driver.findListItem(By.index(5)).press();

      driver.findTextField(By.text("username")).enterText("ddary_mobile");
      driver.findTextField(By.text("password")).enterText("password");
      driver.findButton(By.text("Sign In")).press();

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
