package sh.calaba.driver.model;

/**
 * Interface representing basic waiting operations.
 * 
 * @author ddary
 */
public interface WaitingSupport {
  public void waitForProgressCloses();

  public void waitForCurrentDialogCloses();

  public void waitFor(By by);
  
  public void waitFor(Integer seconds);
}
