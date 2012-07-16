package sh.calaba.driver.client.model.impl;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.SpinnerSupport;

public class SpinnerImpl extends RemoteObject implements SpinnerSupport {
  private By by;

  public SpinnerImpl(RemoteCalabashAndroidDriver driver, By by) {
    super(driver);
    this.by = by;
  }

  @Override
  public void selectSpinnerItem(String text) {
    if (by instanceof By.Id) {
      executeCalabashCommand(CalabashCommands.SELECT_ITEM_BY_NAME, by.getIndentifier(), text);
    } else if (by instanceof By.ContentDescription) {
      executeCalabashCommand(CalabashCommands.SELECT_ITEM_FROM_NAMED_SPINNER, by.getIndentifier(),
          text);
    } else {
      throw new IllegalArgumentException("By type not yet supported");
    }

  }

}
