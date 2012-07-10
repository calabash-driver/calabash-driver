package sh.calaba.driver.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import sh.calaba.utils.AdbConnection;

public class CalabashAdbCmdRunner {
	private static AdbConnection adbConnection = getAdbConnection();

	protected static AdbConnection getAdbConnection() {
		return new AdbConnection();
	}

	/**
	 * TODO ddary: make this configurable
	 * 
	 * @param deviceId
	 */
	public static void switchToEbayQA(String deviceId) {
		List<String> commandLineFwd = new ArrayList<String>();
		if (deviceId != null) {
			commandLineFwd.add("-s");
			commandLineFwd.add(deviceId);
		}
		commandLineFwd.add("shell");
		commandLineFwd.add("setprop");
		// USE THIS for prior 1.8
		//commandLineFwd.add("log.tag.eBayQAServerSwitch");
		
		//Use this for post 1.8 releases
		commandLineFwd.add("log.tag.fwUseQaServers");
		
		
		commandLineFwd.add("DEBUG");

		adbConnection.runProcess(commandLineFwd, "Switching to eBay QA Env",
				true);
	}

	public static void installAPKFile(String pathToAPK, String deviceId) {
		List<String> commandLineFwd = new ArrayList<String>();
		if (deviceId != null) {
			commandLineFwd.add("-s");
			commandLineFwd.add(deviceId);
		}
		commandLineFwd.add("install");
		commandLineFwd.add("-r");
		commandLineFwd.add(pathToAPK);

		adbConnection.runProcess(commandLineFwd, "about to start install APK",
				true);
	}

	public static Thread startCalabashServer(final String deviceId) {
		Thread instrumentationThread = new Thread(new Runnable() {

			@Override
			public void run() {
				List<String> commandLineFwd = new ArrayList<String>();
				if (deviceId != null) {
					commandLineFwd.add("-s");
					commandLineFwd.add(deviceId);
				}
				commandLineFwd.add("shell");

				commandLineFwd.add("am");
				commandLineFwd.add("instrument");
				commandLineFwd.add("-e class");
				commandLineFwd
						.add("sh.calaba.instrumentationbackend.InstrumentationBackend");
				commandLineFwd.add("-w");
				commandLineFwd
						.add("com.ebay.mobile.test/android.test.InstrumentationTestRunner");

				adbConnection.runProcess(commandLineFwd,
						"about to start CalabashServer", false);

			}
		});
		instrumentationThread.start();

		// needed because the process will not end, but wait is needed
		waitForCalabashServerOnDevice(deviceId);
		return instrumentationThread;
	}

	public static void waitForCalabashServerOnDevice(final String deviceId) {
		(new CalabashAdbCmdRunner().new CalabashServerWaiter(adbConnection,
				deviceId)).run();
		;
	}

	public static void activatePortForwarding(int local, int remote,
			String deviceId) {
		List<String> commandLineFwd = new ArrayList<String>();
		if (deviceId != null) {
			commandLineFwd.add("-s");
			commandLineFwd.add(deviceId);
		}
		commandLineFwd.add("forward");
		commandLineFwd.add("tcp:" + local);
		commandLineFwd.add("tcp:" + remote);
		adbConnection.runProcess(commandLineFwd,
				"about to forward: local port: " + local + " to remote port: "
						+ remote, true);
	}

	public static void deleteSavedAppData(String appBasePackage, String deviceId) {
		List<String> commandLineFwd = new ArrayList<String>();
		if (deviceId != null) {
			commandLineFwd.add("-s");
			commandLineFwd.add(deviceId);
		}
		commandLineFwd.add("shell");
		commandLineFwd.add("pm");
		commandLineFwd.add("clear");
		commandLineFwd.add(appBasePackage);
		adbConnection.runProcess(commandLineFwd,
				"about to delete the saved app data of the app with the base package: "
						+ appBasePackage, true);
	}

	public class CalabashServerWaiter implements Runnable {
		private Lock lock = new ReentrantLock();
		private Condition cv = lock.newCondition();
		private AdbConnection adbConnection;
		private String deviceId;

		CalabashServerWaiter(AdbConnection con, String deviceId) {
			this.adbConnection = con;
			this.deviceId = deviceId;
		}

		private boolean isPortBound() {
			List<String> commandLineFwd = new ArrayList<String>();
			if (deviceId != null) {
				commandLineFwd.add("-s");
				commandLineFwd.add(deviceId);
			}
			commandLineFwd.add("shell");
			commandLineFwd.add("netstat");
			String result = adbConnection.runProcess(commandLineFwd,
					"wait for calabash-server on mobile device", true);
			if (result == null) {
				return false;
			} else if (result.contains(":::7101")) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void run() {
			lock.lock();

			try {
				boolean portIsNotBound = !isPortBound();
				while (portIsNotBound) {

					cv.await(2, TimeUnit.SECONDS);
					portIsNotBound = !isPortBound();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				lock.unlock();
			}

		}
	}
}
