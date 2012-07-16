package sh.calaba.driver.model;

import java.io.File;

/**
 * 
 * @author ddary
 *
 */
public interface ViewSupport {
  public void click();

  public void longPress();

  public void press();

  public void waitFor();

  public void scrollUp();

  public void scrollDown();

  public File takeScreenshot(String path);
  
  public String getText();
  
  public Boolean isEnabled();
}
