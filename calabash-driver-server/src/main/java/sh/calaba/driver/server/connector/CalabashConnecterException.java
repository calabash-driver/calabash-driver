/*
 * Copyright 2012 calabash-driver committers.
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
package sh.calaba.driver.server.connector;

/**
 * The Class CalabashConnecterException is thrown on Calabash connector issues.
 */
public class CalabashConnecterException extends RuntimeException {
  private static final long serialVersionUID = 2953563072912935718L;

  public CalabashConnecterException(String msg, Throwable t) {
    super(msg, t);
  }

  public CalabashConnecterException(String msg) {
    super(msg);
  }

  public CalabashConnecterException(Throwable t) {
    super(t);
  }
}
