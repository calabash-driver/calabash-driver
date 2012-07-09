package sh.calaba.driver.server.exceptions;

public class CalabashConfigurationException extends Exception {
	private static final long serialVersionUID = 2482901359161347701L;
    public CalabashConfigurationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CalabashConfigurationException(String arg0) {
		super(arg0);
	}

	public CalabashConfigurationException(Throwable arg0) {
		super(arg0);
	}

}
