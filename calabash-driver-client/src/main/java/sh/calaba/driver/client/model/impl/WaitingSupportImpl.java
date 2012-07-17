package sh.calaba.driver.client.model.impl;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.WaitingSupport;

public class WaitingSupportImpl extends RemoteObject implements WaitingSupport {

  public WaitingSupportImpl(RemoteCalabashAndroidDriver driver) {
    super(driver);
  }

  @Override
  public void waitForProgressCloses() {
    executeCalabashCommand(CalabashCommands.WAIT_FOR_NO_PROGRESS_BARS);
  }

  @Override
  public void waitForCurrentDialogCloses() {
    executeCalabashCommand(CalabashCommands.WAIT_FOR_DIALOG_TO_CLOSE);
  }

  @Override
  public void waitFor(By by) {
    if (by instanceof By.ContentDescription) {
      executeCalabashCommand(CalabashCommands.WAIT_FOR_TEXT, by.getIndentifier());
    } else {
      throw new RuntimeException("Not Yet supported");
    }
  }

  @Override
  public void waitFor(Integer seconds) {
    executeCalabashCommand(CalabashCommands.WAIT, String.valueOf(seconds));
  }
}
