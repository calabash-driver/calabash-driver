package sh.calaba.driver.server.support;

import java.util.Properties;

import sh.calaba.driver.server.CalabashAndroidServer;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.support.NanoHTTPD.Response;
import sh.calaba.driver.utils.CalabashAdbCmdRunner;

public class CalabashTestServer {
  private CalabashInstrumentationServerStub instrumentationServer;
  private CalabashAndroidServer server;

  public static final String host = "localhost";
  private static int PORTS = 34777;
  private int port;

  private int getNewPort() {
    return ++PORTS;
  }

  public int getPort() {
    return port;
  }

  public CalabashTestServer() throws Exception {
    port = getNewPort();
    instrumentationServer = new CalabashInstrumentationServerStub(port);
    instrumentationServer
        .registerTestSessionListener(instrumentationServer.new CalabashTestSessionListener() {

          @Override
          public Response executeCalabashCommand(Properties params) {
            String commandString = params.getProperty("json");
            System.out.println("commandString: " + commandString);
            return defaultCalabashCommmandResponse();
          }
        });
    Thread.sleep(3500);
    CalabashLocalNodeConfiguration conf =
        new CalabashLocalNodeConfiguration(CapabilityFactory.anAndroidCapability(), host, port);
    CalabashProxy proxy = new MyTestCalabashProxy(port);
    proxy.setCalabashAdbCmdRunner(new CalabashAdbCmdRunner(new AdbConnectionStub()));
    server = new CalabashAndroidServer();
    server.setProxy(proxy);
    server.start(conf);
  }


  public void stopServer() {
    instrumentationServer.stop();
    try {
      server.stop();
    } catch (Exception e) {
      throw new RuntimeException("error occured while stopping calabash-test server.", e);
    }
  }

  public class MyTestCalabashProxy extends CalabashProxy {
    int port;

    public MyTestCalabashProxy(int port) {
      this.port = port;
    }

    /*
     * (non-Javadoc)
     * 
     * @see sh.calaba.driver.server.CalabashProxy#getNextPortNumber()
     */
    @Override
    protected Integer getNextPortNumber() {
      return port;
    }
  }
}
