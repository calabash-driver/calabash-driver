package sh.calaba.driver.client.model.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.ViewSupport;

public class ViewImpl extends RemoteObject implements ViewSupport {
	private String id = null;

	public ViewImpl(RemoteCalabashAndroidDriver driver, String id) {
		super(driver);
		this.id = id;
	}

	public void clickOnViewById() {
		executeCalabashCommand(CalabashCommands.CLICK_ON_VIEW_BY_ID, id);
	}

	public void longPressOnViewById() {
		executeCalabashCommand(CalabashCommands.LONG_PRESS_ON_VIEW_BY_ID, id);
	}

	public void press() {
		executeCalabashCommand(CalabashCommands.PRESS, id);
	}

	public void waitForViewById() {
		executeCalabashCommand(CalabashCommands.WAIT_FOR_VIEW_BY_ID, id);
	}

	@Override
	public File takeScreenshot(String path) {
		File file = null;
		JSONObject result = executeCalabashCommand(CalabashCommands.TAKE_SCREENSHOT);
		try {
			String base64String = result.getJSONArray("bonusInformation")
					.getString(0);

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
