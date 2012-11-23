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
package sh.calaba.driver.server.command;

import java.lang.reflect.Constructor;

import sh.calaba.driver.exceptions.CalabashException;
import sh.calaba.driver.net.WebDriverLikeCommand;
import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.Handler;
import sh.calaba.driver.server.command.impl.GetSessionCapabilities;
import sh.calaba.driver.server.command.impl.GetSessions;
import sh.calaba.driver.server.command.impl.GetStatus;
import sh.calaba.driver.server.command.impl.NewSession;
import sh.calaba.driver.server.command.impl.StopSession;

public enum CommandMapping {
  NEW_SESSION(NewSession.class),
  GET_SESSION(GetSessionCapabilities.class),
  GET_SESSIONS(GetSessions.class),
  DELETE_SESSION(StopSession.class),
  GET_STATUS(GetStatus.class),

  // ## Calabash Commands
  BUTTON(CalabashCommandHandler.class),
  VIEW(CalabashCommandHandler.class),
  TEXT_FIELD(CalabashCommandHandler.class),
  WAIT(CalabashCommandHandler.class),
  L10N_SUPPORT(CalabashCommandHandler.class),
  SCREENSHOT_WITH_NAME(CalabashCommandHandler.class),
  SEARCH(CalabashCommandHandler.class),
  SPINNER(CalabashCommandHandler.class),
  DEVICE(CalabashCommandHandler.class),
  LIST_ITEM(CalabashCommandHandler.class),
  WEB_VIEW(CalabashCommandHandler.class);
  

  private WebDriverLikeCommand command;
  private final Class<? extends Handler> handlerClass;

  private CommandMapping(Class<? extends Handler> handlerClass) {
    this.command = WebDriverLikeCommand.valueOf(this.name());
    this.handlerClass = handlerClass;
  }

  public static CommandMapping get(WebDriverLikeCommand wdlc) {
    for (CommandMapping cm : values()) {
      if (cm.command == wdlc) {
        return cm;
      }
    }
    throw new CalabashException("The requested cammnad is currently not mapped : " + wdlc);
  }

  public Handler createHandler(CalabashProxy proxy, WebDriverLikeRequest request) throws Exception {

    Class<?> clazz = handlerClass;
    if (clazz == null) {
      throw new IllegalArgumentException("handler class is null. incoming request: "
          + request.getPath());
    }

    Object[] args = new Object[] {proxy, request};
    Class<?>[] argsClass = new Class[] {CalabashProxy.class, WebDriverLikeRequest.class};

    Constructor<?> c = clazz.getConstructor(argsClass);
    Object handler = c.newInstance(args);
    return (Handler) handler;
  }
}
