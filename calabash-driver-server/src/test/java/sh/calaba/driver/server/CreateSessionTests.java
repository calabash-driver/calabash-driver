package sh.calaba.driver.server;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.exceptions.SessionNotCreatedException;
import sh.calaba.driver.net.HttpClientFactory;
import sh.calaba.driver.server.support.CalabashTestServer;
import sh.calaba.driver.server.support.CapabilityFactory;

public class CreateSessionTests {
  public static final String host = "localhost";

  @DataProvider(name = "testserver")
  public static Object[][] createTestServer() throws Exception {
    return new Object[][] {new Object[] {new CalabashTestServer()}};
  }


  @Test(dataProvider = "testserver", expectedExceptions = {SessionNotCreatedException.class}, expectedExceptionsMessageRegExp = ".*Driver does not support desired capability.*")
  public void sessionIsNotInitCausedByFromCapa(CalabashTestServer server) {
    RemoteCalabashAndroidDriver driver = null;
    try {
      CalabashCapabilities capa = new CalabashCapabilities();
      capa.setAut(CapabilityFactory.ANY_VALUE);
      capa.setLocale(CapabilityFactory.ANY_VALUE + "Hello");
      driver =
          new RemoteCalabashAndroidDriver("http://" + host + ":" + server.getPort() + "/wd/hub",
              capa.getRawCapabilities());

      Assert.assertTrue(driver.getSession().getSessionId() != null,
          "Expectation is that a new session ID is created.");

    } finally {
      if (driver != null) {
        driver.quit();
        server.stopServer();
      }
    }
  }

  @Test(dataProvider = "testserver", dependsOnMethods = {"getAllSessionCapabilities"})
  public void getAllSessionCapabilities(CalabashTestServer server) throws Exception {
    RemoteCalabashAndroidDriver driver = null;
    try {
      CalabashCapabilities capa = CapabilityFactory.anAndroidCapability();
      driver =
          new RemoteCalabashAndroidDriver("http://" + host + ":" + server.getPort() + "/wd/hub",
              capa.getRawCapabilities());
      Assert.assertTrue(driver.getSession().getSessionId() != null,
          "Expectation is that a new session ID is created.");

      HttpClient client = HttpClientFactory.getClient();
      HttpGet getCapa =
          new HttpGet("http://" + host + ":" + server.getPort() + "/wd/hub/sessions/");
      HttpResponse response = client.execute(getCapa);
      String responseText = IOUtils.toString(response.getEntity().getContent());
      JSONObject jsonResponse = new JSONObject(responseText);
      Assert.assertEquals(jsonResponse.has("sessionId"), false);
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
        server.stopServer();
      }
    }
  }

  @Test(dataProvider = "testserver", dependsOnMethods = {"sessionIsNotInitCausedByFromCapa"})
  public void getSessionCapabilities(CalabashTestServer server) throws Exception {
    RemoteCalabashAndroidDriver driver = null;
    try {
      CalabashCapabilities capa = CapabilityFactory.anAndroidCapability();
      driver =
          new RemoteCalabashAndroidDriver("http://" + host + ":" + server.getPort() + "/wd/hub",
              capa.getRawCapabilities());
      Assert.assertTrue(driver.getSession().getSessionId() != null,
          "Expectation is that a new session ID is created.");

      HttpClient client = HttpClientFactory.getClient();
      HttpGet getCapa =
          new HttpGet("http://" + host + ":" + server.getPort() + "/wd/hub/session/"
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
        server.stopServer();
      }
    }
  }
}
