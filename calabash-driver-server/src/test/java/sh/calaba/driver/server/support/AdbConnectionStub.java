package sh.calaba.driver.server.support;

import java.util.List;

import sh.calaba.driver.utils.CalabashAdbCmdRunner;
import sh.calaba.utils.AdbConection;
import sh.calaba.utils.AdbConnetionException;

public class AdbConnectionStub implements AdbConection {

  @Override
  public String runProcess(List<String> abdParameter, String name, boolean confirmExitValue)
      throws AdbConnetionException {
    for (String parameter : abdParameter) {
      if (parameter.contains("netstat")) {
        return ":::" + CalabashAdbCmdRunner.CALABASH_INTERNAL_PORT;
      }
    }
    return null;
  }

  @Override
  public void confirmExitValueIs(int expected, Process process, String command) {
    // TODO Auto-generated method stub

  }

}
