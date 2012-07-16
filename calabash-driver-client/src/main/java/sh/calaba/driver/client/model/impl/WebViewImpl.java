package sh.calaba.driver.client.model.impl;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.WebViewSupport;

public class WebViewImpl extends RemoteObject implements WebViewSupport {
  public static final String CSS = "css";
  private By.CSS css;

  public WebViewImpl(RemoteCalabashAndroidDriver driver, By.CSS css) {
    super(driver);
    this.css = css;
  }

  @Override
  public void enterText(String text) {
    executeCalabashCommand(CalabashCommands.SET_SET, CSS, css.getIndentifier(), text);
  }

  @Override
  public void click() {
    executeCalabashCommand(CalabashCommands.TOUCH, CSS, css.getIndentifier());
  }
}
