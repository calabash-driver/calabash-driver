package sh.calaba.driver.client.model.impl;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.ListItemSupport;

public class ListItemImpl extends RemoteObject implements ListItemSupport {

  private By by;

  public ListItemImpl(RemoteCalabashAndroidDriver driver, By by) {
    super(driver);
    this.by = by;
  }

  @Override
  public void press() {
    if (by instanceof By.Index) {
      executeCalabashCommand(CalabashCommands.PRESS_LIST_ITEM, by.getIndentifier(), "0");
    } else if (by instanceof By.ContentDescription) {
      executeCalabashCommand(CalabashCommands.PRESS_LIST_ITEM_TEXT, by.getIndentifier());
    } else if (by instanceof By.Id) {
      executeCalabashCommand(CalabashCommands.PRESS, by.getIndentifier());
    } else {
      throw new IllegalArgumentException("By not available.");
    }

  }

  @Override
  public void longPress() {
    if (by instanceof By.Index) {
      executeCalabashCommand(CalabashCommands.LONG_PRESS_LIST_ITEM, by.getIndentifier());
    } else {
      throw new IllegalArgumentException("By not available.");
    }
  }
}
