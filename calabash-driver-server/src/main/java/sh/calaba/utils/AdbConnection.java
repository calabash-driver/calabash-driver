package sh.calaba.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * adb -s 304D19CE983D818E -e shell getprop ro.product.model
 * 
 * @author ddary
 * 
 */
public class AdbConnection {
	public static final String ANDROID_SDK_PATH_KEY = "android.sdk.path";
	private String pathToAdb = null;

	public AdbConnection(String pathToAdb) {
		this.pathToAdb = pathToAdb;
	}

	/**
	 * Reading the ADB path from the property file
	 */
	public AdbConnection() {
		String androidHome = new PropertyReader("calabash.properties")
				.getProperty(ANDROID_SDK_PATH_KEY);

		if (androidHome == null) {
			throw new RuntimeException(
					"System property 'android.sdk.path' not found!");
		}
		boolean isWindows = System.getProperty("os.name").toLowerCase()
				.indexOf("win") >= 0;
		String executableSuffix = isWindows ? ".exe" : "";
		this.pathToAdb = androidHome + File.separator+"platform-tools"+File.separator+"adb" + executableSuffix;
	}

	/**
	 * 
	 * @param abdParameter
	 * @param name
	 * @param confirmExitValue
	 * @throws AdbConnetionException
	 */
	public String runProcess(List<String> abdParameter, String name,
			boolean confirmExitValue) throws AdbConnetionException {
		abdParameter.add(0, this.pathToAdb);
		ProcessBuilder processBuilder = new ProcessBuilder(abdParameter);
		processBuilder.redirectErrorStream(true);
		// processBuilder.
		System.out.println("Process '" + name + "' is about to start: "
				+ processBuilder.command());
		try {
			Process process = processBuilder.start();

			if (confirmExitValue) {
				confirmExitValueIs(0, process);
			}
			String out = IOUtils.toString(process.getInputStream());
			return out;
		} catch (IOException exception) {
			System.err.println("Error occured: ");
			throw new AdbConnetionException(
					"An IOException occurred when starting ADB.");
		}
	}

	/*
	 * just for debugging private static void pipeOutput(Process process) {
	 * pipe(process.getErrorStream(), System.err);
	 * pipe(process.getInputStream(), System.out); }
	 * 
	 * private static void pipe(final InputStream src, final PrintStream dest) {
	 * new Thread(new Runnable() { public void run() { try { byte[] buffer = new
	 * byte[1024]; for (int n = 0; n != -1; n = src.read(buffer)) {
	 * dest.write(buffer, 0, n); } } catch (IOException e) { // just exit } }
	 * }).start(); }
	 */

	/**
	 * 
	 * Confirms the exit value of a process is equal to an expected value, and
	 * throws an exception if it is not. This method will also wait for the
	 * process to finish before checking the exit value.
	 * 
	 * @param expected
	 *            the expected exit value, usually {@code 0}
	 * @param process
	 *            the process whose exit value will be confirmed
	 * @throws AdbException
	 *             if the exit value was not equal to {@code expected}
	 */
	public static void confirmExitValueIs(int expected, Process process) {
		while (true) {
			try {
				process.waitFor();
				System.out.println("Waiting for process...");
				break;
			} catch (InterruptedException exception) {
				// do nothing, try to wait again
			}
		}
		System.out.println(" process exit value: " + process.exitValue());
		if (expected != process.exitValue()) {
			throw new AdbConnetionException("Exit value of process was "
					+ process.exitValue() + " but expected " + expected);
		}
	}
}
