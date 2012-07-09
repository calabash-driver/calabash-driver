package sh.calaba.driver.model;

import java.io.File;


public interface ViewSupport {
	public void clickOnViewById();

	public void longPressOnViewById() ;

	public void press() ;

	public void waitForViewById();
	
	public File takeScreenshot(String path);
}
