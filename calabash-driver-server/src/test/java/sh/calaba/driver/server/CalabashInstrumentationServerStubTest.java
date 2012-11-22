package sh.calaba.driver.server;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import sh.calaba.driver.server.support.CalabashInstrumentationServerStub;
import sh.calaba.driver.server.support.NanoHTTPD.Response;

public class CalabashInstrumentationServerStubTest {
  public static final String hostname = "localhost";

  private CalabashInstrumentationServerStub instrumentationServer = null;

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
  }

  @Test
  public void runServer() throws Exception {
    String path = "/ready";
    HttpPost postRequest =
        new HttpPost("http://" + hostname + ":" + CalabashProxy.DEFAULT_CALABASH_ANDROID_LOCAL_PORT
            + path);


    postRequest.addHeader("Content-Type", "application/json;charset=utf-8");
    HttpClient httpClient = new DefaultHttpClient();
    HttpResponse response = httpClient.execute(postRequest);
    StringWriter writer = new StringWriter();
    IOUtils.copy(response.getEntity().getContent(), writer);

    Assert.assertEquals(writer.toString(), "true");

  }

  @AfterClass
  public void shutdown() {
    instrumentationServer.stop();
  }
}
