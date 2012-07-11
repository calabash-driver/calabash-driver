package sh.calaba.driver.client.model.impl;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.L10nSupport;

public class L10nElementImpl extends RemoteObject implements L10nSupport {
	private By.L10nElement id = null;

	public L10nElementImpl(RemoteCalabashAndroidDriver driver, By.L10nElement id) {
		super(driver);
		this.id = id;
	}

	@Override
	public void press() {
		if (id.getType() == null) {
			executeCalabashCommand(CalabashCommands.PRESS_L10N_ELEMENT,
					id.getIndentifier());
		} else {
			executeCalabashCommand(CalabashCommands.PRESS_L10N_ELEMENT,
					id.getIndentifier(), id.getType().name());
		}
	}

	@Override
	public void waitFor() {
		executeCalabashCommand(CalabashCommands.WAIT_FOR_L10N_ELEMENT,
				id.getIndentifier());
	}
}
