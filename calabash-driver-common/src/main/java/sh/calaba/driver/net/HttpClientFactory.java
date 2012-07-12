package sh.calaba.driver.net;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

public class HttpClientFactory {

  public static DefaultHttpClient getClient() {
    DefaultHttpClient client = new DefaultHttpClient();
    client.setRedirectStrategy(new MyRedirectHandler());
    return client;
  }

  static class MyRedirectHandler implements RedirectStrategy {

    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
        throws ProtocolException {
      return false;
    }

    public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response,
        HttpContext context) throws ProtocolException {
      return null;
    }
  }
}
