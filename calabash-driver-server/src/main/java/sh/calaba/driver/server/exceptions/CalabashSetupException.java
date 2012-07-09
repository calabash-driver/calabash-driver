package sh.calaba.driver.server.exceptions;

public class CalabashSetupException extends RuntimeException {
	public CalabashSetupException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CalabashSetupException(String arg0) {
		super(arg0);
	}

	public CalabashSetupException(Throwable arg0) {
		super(arg0);
	}

	private static final long serialVersionUID = -5172256147389776657L;

}
