package sh.calaba.driver.model;

import java.io.File;

public interface ViewSupport {
	public void click();

	public void longPress();

	public void press();

	public void waitFor();

	public void pressL10nElement();

	public void waitForL10nElement();

	@Deprecated
	public File takeScreenshot(String path);
}
