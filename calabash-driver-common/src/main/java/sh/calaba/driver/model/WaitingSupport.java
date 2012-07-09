package sh.calaba.driver.model;

public interface WaitingSupport {
	public void waitForProgressCloses();

	public void waitForCurrentDialogCloses();
	public void waitFor(By by);
}
