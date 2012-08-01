package sh.calaba.driver.client;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.model.By;

public class ClientEndToEndTest {

  private String host = "127.0.0.1";
  private int port = 4444;

  @Test
  public void testJSON() throws Exception {
    String jsonSource = "{\"message\":\"\",\"bonusInformation\":[],\"success\":true}";
    JSONObject json = new JSONObject(jsonSource);
    Assert.assertEquals(json.get("success"), true);
  }

  @Test(enabled = false)
  public void scriptStartsAndRegisterToServer() {
    RemoteCalabashAndroidDriver driver = null;
    try {

      driver = new RemoteCalabashAndroidDriver(host, port, nexus());

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

  public CalabashCapabilities note() {
    return CalabashCapabilities.android("eBayMobile:1.7.0", "2.3", "en_GB", "304D19CE983D818E",
        "GT-N7000", "com.ebay.mobile");
  }

  public CalabashCapabilities nexus() {
    return CalabashCapabilities.android("eBayMobile:1.7.0", "4.0.4", "de_DE", "0149948604012003",
        "GalaxyNexus", "com.ebay.mobile");
  }
}
