package sh.calaba.driver.server.support;

import java.io.File;

public class CalabashWebNodeConfigStub extends NanoHTTPD{

  public CalabashWebNodeConfigStub() {
    super(8080, new File("src/test/resources/nodeconfig"));
  }

}
