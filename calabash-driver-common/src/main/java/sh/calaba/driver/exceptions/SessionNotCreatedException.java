package sh.calaba.driver.exceptions;

public class SessionNotCreatedException extends CalabashException {
  private static final long serialVersionUID = -310192560802477665L;

  public SessionNotCreatedException(String message) {
    super(message);
  }

  public SessionNotCreatedException(String message, Throwable t) {
    super(message, t);
  }
}
