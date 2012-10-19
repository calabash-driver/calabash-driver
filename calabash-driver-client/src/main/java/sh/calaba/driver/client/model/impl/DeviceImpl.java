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
package sh.calaba.driver.client.model.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import sh.calaba.driver.client.CalabashCommands;
import sh.calaba.driver.client.RemoteCalabashAndroidDriver;
import sh.calaba.driver.exceptions.CalabashException;
import sh.calaba.driver.model.By.Text;
import sh.calaba.driver.model.DeviceSupport;

/**
 * Implementation to support interactions with the device.
 * 
 * @author ddary
 */
public class DeviceImpl extends RemoteObject implements DeviceSupport {

  public DeviceImpl(RemoteCalabashAndroidDriver driver) {
    super(driver);
  }

  @Override
  public void scrollUp() {
    executeCalabashCommand(CalabashCommands.SCROLL_UP);
  }

  @Override
  public void scrollDown() {
    executeCalabashCommand(CalabashCommands.SCROLL_DOWN);
  }

  @Override
  public File takeScreenshot(String path) {
    File file = null;
    JSONObject result = executeCalabashCommand(CalabashCommands.TAKE_SCREENSHOT);
    if (result == null || !result.has("bonusInformation")) {
      throw new CalabashException("An error occured while taking a screenshot.");
    }
    try {
      JSONArray bonusInformation = result.getJSONArray("bonusInformation");
      String base64String = bonusInformation.getString(0);

      byte[] img64 = Base64.decodeBase64(base64String);
      file = new File(path);
      FileOutputStream os = new FileOutputStream(file);
      os.write(img64);
      os.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return file;
  }

  @Override
  public void pressContextMenuItem(Text text) {
    executeCalabashCommand(CalabashCommands.PRESS_LONG_ON_TEXT, text.getIdentifier());
  }
}
