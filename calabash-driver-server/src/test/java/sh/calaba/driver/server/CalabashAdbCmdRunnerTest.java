package sh.calaba.driver.server;

import org.testng.annotations.Test;

import sh.calaba.driver.utils.CalabashAdbCmdRunner;

public class CalabashAdbCmdRunnerTest {
	@Test
	public void testNestat() {

		CalabashAdbCmdRunner.waitForCalabashServerOnDevice("304D19CE983D818E");
	}

}
