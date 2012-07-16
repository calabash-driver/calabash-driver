package sh.calaba.driver.server.command;

import java.lang.reflect.Constructor;

import sh.calaba.driver.net.WebDriverLikeCommand;
import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.Handler;
import sh.calaba.driver.server.command.impl.GetCapabilities;
import sh.calaba.driver.server.command.impl.GetStatus;
import sh.calaba.driver.server.command.impl.NewSession;
import sh.calaba.driver.server.command.impl.StopSession;

public enum CommandMapping {

  NEW_SESSION(NewSession.class), GET_SESSION(GetCapabilities.class), DELETE_SESSION(
      StopSession.class), GET_STATUS(GetStatus.class),

  // ## Calabash Commands
  BUTTON(CalabashCommandHandler.class), VIEW(CalabashCommandHandler.class), TEXT_FIELD(
      CalabashCommandHandler.class), WAIT(CalabashCommandHandler.class), L10N_SUPPORT(
      CalabashCommandHandler.class), SCREENSHOT_WITH_NAME(CalabashCommandHandler.class), SEARCH(
      CalabashCommandHandler.class), SPINNER(CalabashCommandHandler.class), LIST_ITEM(
      CalabashCommandHandler.class), WEB_VIEW(CalabashCommandHandler.class);


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
    throw new RuntimeException("not mapped : " + wdlc);
  }

  public Handler createHandler(CalabashProxy proxy, WebDriverLikeRequest request) throws Exception {

    Class<?> clazz = handlerClass;
    if (clazz == null) {
      throw new RuntimeException("handler NI");
    }

    Object[] args = new Object[] {proxy, request};
    Class<?>[] argsClass = new Class[] {CalabashProxy.class, WebDriverLikeRequest.class};

    Constructor<?> c = clazz.getConstructor(argsClass);
    Object handler = c.newInstance(args);
    return (Handler) handler;

  }

}
