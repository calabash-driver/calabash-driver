package sh.calaba.driver.client.model.impl;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.ButtonSupport;
import sh.calaba.driver.model.By;

public class ButtonImpl extends RemoteObject implements ButtonSupport {
  private By by;

  public ButtonImpl(RemoteCalabashAndroidDriver driver, By by) {
    super(driver);
    this.by = by;
  }

  @Override
  public void press() {
    if (by instanceof By.Index) {
      executeCalabashCommand(CalabashCommands.PRESS_BUTTON_NUMBER, by.getIndentifier());
    } else if (by instanceof By.ContentDescription) {
      executeCalabashCommand(CalabashCommands.PRESS, by.getIndentifier());
    } else if (by instanceof By.Id) {
      executeCalabashCommand(CalabashCommands.PRESS, by.getIndentifier());
    } else {
      throw new IllegalArgumentException("By not available.");
    }
  }
}
