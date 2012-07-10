package sh.calaba.driver.model;

import java.io.File;

public interface ViewSupport {
	public void click();

	public void longPress();

	public void press();

	public void waitFor();

	@Deprecated
	public File takeScreenshot(String path);
}
