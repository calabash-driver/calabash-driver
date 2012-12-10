package sh.calaba.driver.server.support;

import java.io.File;

public class CalabashWebNodeConfigStub extends NanoHTTPD{

  public CalabashWebNodeConfigStub() {
    super(8081, new File("src/test/resources/nodeconfig"));
  }

}
