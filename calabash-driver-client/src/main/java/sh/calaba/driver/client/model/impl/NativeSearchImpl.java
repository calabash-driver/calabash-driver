package sh.calaba.driver.client.model.impl;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.model.By;
import sh.calaba.driver.model.NativeSearchSupport;

public class NativeSearchImpl extends RemoteObject implements
		NativeSearchSupport {
	private By.Id by;

	public NativeSearchImpl(RemoteCalabashAndroidDriver driver, By.Id id) {
		super(driver);
		this.by = id;
	}

	@Override
	public void text(String searchTerm) {
		executeCalabashCommand(CalabashCommands.SEARCH_FOR,
				by.getIndentifier(), searchTerm);
	}

}
