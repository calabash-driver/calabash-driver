package sh.calaba.driver.net;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

public class Path {

  private String path;
  private Set<String> variables = new HashSet<String>();

  public static final String SESSION_ID = ":sessionId";
  public static final String REFERENCE = ":reference";



  public Path(WebDriverLikeCommand command) {
    this.path = command.path();

    String[] pieces = path.split("/");
    for (String piece : pieces) {
      if (piece.startsWith(":")) {
        variables.add(piece);
      }
    }
  }

  public Path withSession(String sessionId) {
    validateAndReplace(SESSION_ID, sessionId);
    return this;
  }

  public Path withReference(String reference) {
    validateAndReplace(REFERENCE, reference);
    return this;
  }



  private void validateAndReplace(String variable, String value) {
    validateContains(variable);
    replace(variable, value);
  }

  private void replace(String variable, String value) {
    path = path.replace(variable, value);
  }

  private void validateContains(String variable) {
    if (!path.contains(variable)) {
      throw new InvalidParameterException(path + " doesn't have " + variable + " as a variable.");
    }
  }

  public String getPath() {
    return path;
  }



}
