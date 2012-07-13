package sh.calaba.driver.client.model.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.ViewSupport;

public class ViewImpl extends RemoteObject implements ViewSupport {
  private By.Id id = null;

  public ViewImpl(RemoteCalabashAndroidDriver driver, By.Id id) {
    super(driver);
    this.id = id;
  }

  public ViewImpl(RemoteCalabashAndroidDriver driver) {
    super(driver);
  }

  public void click() {
    assertIdNotNull();
    executeCalabashCommand(CalabashCommands.CLICK_ON_VIEW_BY_ID, id.getIndentifier());
  }

  private void assertIdNotNull() {
    if (id == null) {
      throw new IllegalArgumentException("Instance variable id cannot be null.");
    }
  }

  public void longPress() {
    assertIdNotNull();
    executeCalabashCommand(CalabashCommands.LONG_PRESS_ON_VIEW_BY_ID, id.getIndentifier());
  }

  public void press() {
    assertIdNotNull();
    executeCalabashCommand(CalabashCommands.PRESS, id.getIndentifier());
  }

  public void waitFor() {
    assertIdNotNull();
    executeCalabashCommand(CalabashCommands.WAIT_FOR_VIEW_BY_ID, id.getIndentifier());
  }

  @Override
  public void scrollUp() {
    executeCalabashCommand(CalabashCommands.SCROLL_UP);
  }

  @Override
  public void scrollDown() {
    executeCalabashCommand(CalabashCommands.SCROLL_DOWN);
  }

  public File takeScreenshot(String path) {
    File file = null;
    JSONObject result = executeCalabashCommand(CalabashCommands.TAKE_SCREENSHOT);
    try {
      String base64String = result.getJSONArray("bonusInformation").getString(0);

      byte[] img64 = Base64.decodeBase64(base64String);
      file = new File(path);
      FileOutputStream os = new FileOutputStream(file);
      os.write(img64);
      os.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return file;
  }
}
