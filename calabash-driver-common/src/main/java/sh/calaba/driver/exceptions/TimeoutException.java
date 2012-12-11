package sh.calaba.driver.exceptions;

public class TimeoutException extends CalabashException {
  private static final long serialVersionUID = 6085933309287215954L;

  public TimeoutException(String message) {
    super(message);
  }

  public TimeoutException(String message, Throwable t) {
    super(message, t);
  }

}
