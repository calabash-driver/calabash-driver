package sh.calaba.driver.client;

import java.io.File;
import java.util.Map;

import sh.calaba.driver.CalabashCapabilities;
import sh.calaba.driver.client.model.impl.ButtonImpl;
import sh.calaba.driver.client.model.impl.L10nElementImpl;
import sh.calaba.driver.client.model.impl.ListItemImpl;
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
	
	public L10nSupport l10nElement(By.L10nElement id){
		return new L10nElementImpl(this, id);
	}

	public File takeScreenshot(String path) {
		// TODO fix this
		return new ViewImpl(this).takeScreenshot(path);
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
