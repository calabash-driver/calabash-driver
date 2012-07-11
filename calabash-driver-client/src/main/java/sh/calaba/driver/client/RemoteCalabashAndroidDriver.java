package sh.calaba.driver.client;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.client.model.impl.ButtonImpl;
import sh.calaba.driver.client.model.impl.L10nElementImpl;
import sh.calaba.driver.client.model.impl.ListItemImpl;
import sh.calaba.driver.client.model.impl.RemoteObject;
import sh.calaba.driver.client.model.impl.TextFieldImpl;
import sh.calaba.driver.client.model.impl.ViewImpl;
import sh.calaba.driver.client.model.impl.WaitingSupportImpl;
import sh.calaba.driver.model.ButtonSupport;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.CalabashAndroidDriver;
import sh.calaba.driver.model.L10nSupport;
import sh.calaba.driver.model.ListItemSupport;
import sh.calaba.driver.model.TextFieldSupport;
import sh.calaba.driver.model.ViewSupport;
import sh.calaba.driver.model.WaitingSupport;
import sh.calaba.driver.net.Path;
import sh.calaba.driver.net.WebDriverLikeCommand;
import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;

public class RemoteCalabashAndroidDriver extends CalabashAndroidDriver {

	public RemoteCalabashAndroidDriver(String remoteURL,
			Map<String, Object> capabilities) {
		super(remoteURL, capabilities);
	}

	public RemoteCalabashAndroidDriver(String host, Integer port,
			CalabashCapabilities capa) {
		super("http://" + host + ":" + port + "/wd/hub", capa
				.getRawCapabilities());
	}

	public TextFieldSupport textField(By by) {
		return new TextFieldImpl(this, by);
	}

	public ButtonSupport button(By by) {
		return new ButtonImpl(this, by);
	}

	public ListItemSupport listItem(By by) {
		return new ListItemImpl(this, by);
	}

	public WaitingSupport waitFor() {
		return new WaitingSupportImpl(this);
	}

	public ViewSupport view(By id) {
		if (id instanceof By.Id) {
			return new ViewImpl(this, (By.Id) id);
		} else {
			throw new IllegalArgumentException(
					"Only By.Id is supported for views.");
		}
	}

	public L10nSupport l10nElement(By.L10nElement id) {
		return new L10nElementImpl(this, id);
	}

	public File takeScreenshot(String path) {
		JSONObject result = getJSONResult(WebDriverLikeCommand.SCREENSHOT_WITH_NAME);

		File file = null;
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

	private JSONObject getJSONResult(WebDriverLikeCommand command) {

		String genericPath = command.path();
		String path = genericPath.replace(":sessionId", getSession()
				.getSessionId());
		WebDriverLikeRequest request = new WebDriverLikeRequest(
				command.method(), path, new JSONObject());
		WebDriverLikeResponse response;
		try {
			response = execute(request);
			return ((JSONObject) response.getValue());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}

	public void scrollUp() {
		// TODO fix this
		new ViewImpl(this).scrollUp();
	}

	public void scrollDown() {
		// TODO fix this
		new ViewImpl(this).scrollDown();
	}
}
