package sh.calaba.driver.server;

import sh.calaba.driver.net.WebDriverLikeResponse;

public interface Handler {

  public WebDriverLikeResponse handle() throws Exception;
}
