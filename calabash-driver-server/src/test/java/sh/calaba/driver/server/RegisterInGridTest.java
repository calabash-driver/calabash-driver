package sh.calaba.driver.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.net.HttpClientFactory;

public class RegisterInGridTest {
  @Test(enabled = false)
  public void registerInGrid() throws Exception {
    String tmp = "http://127.0.0.1:4444/grid/register";

    HttpClient client = HttpClientFactory.getClient();

    URL registration = new URL(tmp);
    System.out.println("Registering the node to hub :" + registration);

    BasicHttpEntityEnclosingRequest r =
        new BasicHttpEntityEnclosingRequest("POST", registration.toExternalForm());
    JSONObject nodeConfig = getNodeConfig();
    r.setEntity(new StringEntity(nodeConfig.toString()));

    HttpHost host = new HttpHost(registration.getHost(), registration.getPort());
    HttpResponse response = client.execute(host, r);
    if (response.getStatusLine().getStatusCode() != 200) {
      throw new RuntimeException("Error sending the registration request.");
    }

  }

  public JSONObject getNodeConfig() {
    JSONObject res = new JSONObject();
    try {
      res.put("class", "org.openqa.grid.common.RegistrationRequest");
      // res.put("id", id);
      // res.put("name", name);
      // res.put("description", description);
      res.put("configuration", getConfiguration());
      JSONArray caps = new JSONArray();
      for (CalabashCapabilities c : getCalabashCapabilities()) {
        caps.put(c.getRawCapabilities());
      }
      res.put("capabilities", caps);
    } catch (JSONException e) {
      throw new RuntimeException("Error encoding to JSON " + e.getMessage(), e);
    }
    System.out.println("NodeConfig:\n" + res.toString());
    return res;
  }

  public JSONObject getConfiguration() throws JSONException {
    JSONObject config = new JSONObject();

    config.put("port", 5555);
    config.put("register", true);
    config.put("host", "localhost");
    config.put("proxy", "org.openqa.grid.selenium.proxy.DefaultRemoteProxy");
    config.put("maxSession", 2);
    config.put("hubHost", "localhost");
    config.put("role", "node");
    config.put("registerCycle", 5000);
    config.put("hub", "http://localhost:4444/grid/register");
    config.put("hubPort", 4444);
    config.put("url", "http://localhost:5555");
    config.put("remoteHost", "http://localhost:5555");
    return config;
  }

  public List<CalabashCapabilities> getCalabashCapabilities() throws JSONException {
    List<CalabashCapabilities> capabilities = new ArrayList<CalabashCapabilities>();
    capabilities.add(CalabashCapabilities.android("eBayMobile:1.7.0", "2.3", "en_GB",
        "304D19CE983D818E", "GT-N7000", "com.ebay.mobile"));
    capabilities.add(CalabashCapabilities.android("eBayMobile:1.7.0", "4.0.3", "de_DE",
        "0149948604012003", "GalaxyNexus", "com.ebay.mobile"));

    return capabilities;
  }
}
