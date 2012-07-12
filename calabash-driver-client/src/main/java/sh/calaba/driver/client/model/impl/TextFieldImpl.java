package sh.calaba.driver.client.model.impl;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.TextFieldSupport;

public class TextFieldImpl extends RemoteObject implements TextFieldSupport {
  private By by;

  public TextFieldImpl(RemoteCalabashAndroidDriver driver, By by) {
    super(driver);
    this.by = by;
  }

  public void clear() {
    if (by instanceof By.Index) {
      executeCalabashCommand(CalabashCommands.CLEAR_NUMBERED_FIELD, by.getIndentifier());
    } else if (by instanceof By.ContentDescription) {
      executeCalabashCommand(CalabashCommands.CLEAR_NAMED_FIELD, by.getIndentifier());
    } else {
      throw new IllegalArgumentException("By not available.");
    }

  }

  public void enterText(String text) {
    if (by instanceof By.Index) {
      executeCalabashCommand(CalabashCommands.ENTER_TEXT_INTO_NUMBERED_FIELD, text,
          by.getIndentifier());
    } else if (by instanceof By.ContentDescription || by instanceof By.Id) {
      executeCalabashCommand(CalabashCommands.ENTER_TEXT_INTO_NAMED_FIELD, text,
          by.getIndentifier());
    } else {
      throw new IllegalArgumentException("By not available.");
    }
  }
}
