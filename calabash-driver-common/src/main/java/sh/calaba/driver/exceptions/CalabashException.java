package sh.calaba.driver.exceptions;

public class CalabashException extends RuntimeException {
  private static final long serialVersionUID = -1592305571101012889L;

  public CalabashException(String message) {
    super(message);
  }

  public CalabashException(Throwable t) {
    super(t);
  }

  public CalabashException(String message, Throwable t) {
    super(message, t);
  }
}
