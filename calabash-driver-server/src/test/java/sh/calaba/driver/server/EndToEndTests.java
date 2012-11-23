package sh.calaba.driver.server;

import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.exceptions.SessionNotCreatedException;
import sh.calaba.driver.model.By;
import sh.calaba.driver.net.HttpClientFactory;
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
  private int port = 4567;

  @BeforeTest
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

  @Test
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

  @Test(expectedExceptions = {SessionNotCreatedException.class}, expectedExceptionsMessageRegExp = ".*Driver does not support desired capability.*")
  public void sessionIsNotInitCausedByFromCapa() {
    RemoteCalabashAndroidDriver driver = null;
    try {
      CalabashCapabilities capa = new CalabashCapabilities();
      capa.setAut(CapabilityFactory.ANY_VALUE);
      capa.setLocale(CapabilityFactory.ANY_VALUE + "Hello");
      driver =
          new RemoteCalabashAndroidDriver("http://" + host + ":" + port + "/wd/hub",
              capa.getRawCapabilities());

      Assert.assertTrue(driver.getSession().getSessionId() != null,
          "Expectation is that a new session ID is created.");

    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }

  @Test
  public void getAllSessionCapabilities() throws Exception {
    RemoteCalabashAndroidDriver driver = null;
    try {
      CalabashCapabilities capa = CapabilityFactory.anAndroidCapability();
      driver =
          new RemoteCalabashAndroidDriver("http://" + host + ":" + port + "/wd/hub",
              capa.getRawCapabilities());
      Assert.assertTrue(driver.getSession().getSessionId() != null,
          "Expectation is that a new session ID is created.");

      HttpClient client = HttpClientFactory.getClient();
      HttpGet getCapa = new HttpGet("http://" + host + ":" + port + "/wd/hub/sessions/");
      HttpResponse response = client.execute(getCapa);
      String responseText = IOUtils.toString(response.getEntity().getContent());
      JSONObject jsonResponse = new JSONObject(responseText);
      // Assert.assertEquals(jsonResponse.get("sessionId"), JSONObject.NULL);
      Assert.assertEquals(jsonResponse.get("status"), 0);

      JSONArray jsonSessions = jsonResponse.getJSONArray("value");
      Assert.assertEquals(jsonSessions.length(), 1);
      JSONObject firstSession = (JSONObject) jsonSessions.get(0);
      Assert.assertEquals(firstSession.get("id"), driver.getSession().getSessionId());
      Assert.assertEquals(firstSession.getJSONObject("capabilities").get(CalabashCapabilities.AUT),
          capa.getApplication());
    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }

  @Test
  public void getSessionCapabilities() throws Exception {
    RemoteCalabashAndroidDriver driver = null;
    try {
      CalabashCapabilities capa = CapabilityFactory.anAndroidCapability();
      driver =
          new RemoteCalabashAndroidDriver("http://" + host + ":" + port + "/wd/hub",
              capa.getRawCapabilities());
      Assert.assertTrue(driver.getSession().getSessionId() != null,
          "Expectation is that a new session ID is created.");

      HttpClient client = HttpClientFactory.getClient();
      HttpGet getCapa =
          new HttpGet("http://" + host + ":" + port + "/wd/hub/session/"
              + driver.getSession().getSessionId());
      HttpResponse response = client.execute(getCapa);
      String responseText = IOUtils.toString(response.getEntity().getContent());
      JSONObject jsonResponse = new JSONObject(responseText);
      Assert.assertEquals(jsonResponse.get("sessionId"), driver.getSession().getSessionId());
      Assert.assertEquals(jsonResponse.get("status"), 0);

      JSONObject jsonCapa = jsonResponse.getJSONObject("value");
      Assert.assertEquals(jsonCapa.get(CalabashCapabilities.LOCALE), capa.getLocale());
      Assert.assertEquals(jsonCapa.get(CalabashCapabilities.AUT), capa.getApplication());
    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }

  @AfterTest
  public void stopServer() throws Exception {
    instrumentationServer.stop();
    server.stop();
  }
}
