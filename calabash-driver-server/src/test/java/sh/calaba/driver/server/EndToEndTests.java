package sh.calaba.driver.server;

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.server.support.AdbConnectionStub;
import sh.calaba.driver.server.support.CalabashInstrumentationServerStub;
import sh.calaba.driver.server.support.CalabashLocalNodeConfiguration;
import sh.calaba.driver.server.support.CapabilityFactory;
import sh.calaba.driver.server.support.NanoHTTPD.Response;
import sh.calaba.driver.utils.CalabashAdbCmdRunner;

public class EndToEndTests {

  private CalabashAndroidServer server;
  private CalabashInstrumentationServerStub instrumentationServer;
  private String host = "localhost";
  private int port = 4444;

  @BeforeClass
  public void startServer() throws Exception {
    instrumentationServer = new CalabashInstrumentationServerStub();
    instrumentationServer
        .registerTestSessionListener(instrumentationServer.new CalabashTestSessionListener() {

          @Override
          public Response executeCalabashCommand(Properties params) {
            String commandString = params.getProperty("json");
            System.out.println("commandString: " + commandString);
            return defaultCalabashCommmandResponse();
          }
        });
    Thread.sleep(3000);
    CalabashLocalNodeConfiguration conf =
        new CalabashLocalNodeConfiguration(CapabilityFactory.anAndroidCapability(), host, port);
    CalabashProxy proxy = new CalabashProxy();
    proxy.setCalabashAdbCmdRunner(new CalabashAdbCmdRunner(new AdbConnectionStub()));
    server = new CalabashAndroidServer();
    server.setProxy(proxy);
    server.start(conf);
  }

  public void sessionIsInitializedAndCommand() {
    RemoteCalabashAndroidDriver driver = null;
    try {
      CalabashCapabilities capa = CapabilityFactory.anAndroidCapability();
      driver =
          new RemoteCalabashAndroidDriver("http://" + host + ":" + port + "/wd/hub",
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
      }
    }
  }

  @AfterClass
  public void stopServer() throws Exception {
    instrumentationServer.stop();
    server.stop();
  }
}
