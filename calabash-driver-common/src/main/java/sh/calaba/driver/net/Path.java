/*
 * Copyright 2012 ios-driver committers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package sh.calaba.driver.net;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

public class Path {
  private String path;
  private Set<String> variables = new HashSet<String>();

  public static final String SESSION_ID = ":sessionId";

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
