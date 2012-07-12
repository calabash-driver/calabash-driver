package sh.calaba.driver.server.servlet;

import javax.servlet.http.HttpServlet;

import sh.calaba.driver.server.CalabashAndroidServer;
import sh.calaba.driver.server.CalabashProxy;

public abstract class CalabashProxyBasedServlet extends HttpServlet {

  private static final long serialVersionUID = -6028114413114935017L;

  private CalabashProxy calabashProxy;

  public CalabashProxy getCalabashProxy() {
    if (calabashProxy == null) {
      calabashProxy =
          (CalabashProxy) getServletContext().getAttribute(CalabashAndroidServer.SCRIPT_KEY);
    }
    return calabashProxy;
  }
}
