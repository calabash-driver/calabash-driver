package sh.calaba.driver.client;

import static org.easymock.EasyMock.*;

import org.apache.http.HttpResponse;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.protocol.HttpRequestHandler;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.model.By;

public class ClientEndToEndTest {
  private LocalTestServer server;
  private HttpRequestHandler handler;

  @Test
  public void testJSON() throws Exception {
    String jsonSource = "{\"message\":\"\",\"bonusInformation\":[],\"success\":true}";
    JSONObject json = new JSONObject(jsonSource);
    Assert.assertEquals(json.get("success"), true);
  }

  @Test(enabled = true)
  public void scriptStartsAndRegisterToServer() {

    HttpResponse response = createMock(HttpResponse.class); // 2
    //handler.handle(null, null, null)


    RemoteCalabashAndroidDriver driver = null;
    try {

      driver =
          new RemoteCalabashAndroidDriver(server.getServiceAddress().getHostName(), server
              .getServiceAddress().getPort(), nexus());

      driver.listItem(By.index(5)).press();
      driver.button(By.text("Zustimmen")).press();
      driver.button(By.text("Einloggen")).press();
      // home_sign_in_button

      // user_name_autocomplete
      driver.textField(By.text("username")).enterText("ddary_mobile");

      // password_edittext
      driver.textField(By.text("password")).enterText("password");

      // signin_btn
      driver.button(By.text("Sign In")).press();

      // Cancel (button)
    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }

  public CalabashCapabilities nexus() {
    return CalabashCapabilities.android("eBayMobile:1.7.0", "4.0.4", "de_DE", "0149948604012003",
        "GalaxyNexus", "com.ebay.mobile");
  }

  @BeforeClass
  public void startTestServer() throws Exception {
    server = new LocalTestServer(null, null);
    handler = createMock(HttpRequestHandler.class);
    server.register("/wd/hub", handler);

    server.start();
  }

  @AfterClass
  public void shutdownTestServer() {

  }
}
