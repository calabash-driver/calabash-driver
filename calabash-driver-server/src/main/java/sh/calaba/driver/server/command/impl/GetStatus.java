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
package sh.calaba.driver.server.command.impl;

import java.util.Date;

import org.json.JSONObject;

import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;
import sh.calaba.driver.server.CalabashProxy;
import sh.calaba.driver.server.command.BaseCommandHandler;

public class GetStatus extends BaseCommandHandler {
  public GetStatus(CalabashProxy proxy, WebDriverLikeRequest request) {
    super(proxy, request);

  }

  public WebDriverLikeResponse handle() throws Exception {
    // TODO ddary find a better way to implement this
    JSONObject build = new JSONObject();
    build.put("version", "1.0-snapshot");
    build.put("revision", "01");
    build.put("time", new Date(2012, 5, 1).getTime());

    JSONObject os = new JSONObject();
    os.put("arch", "INTEL");
    os.put("name", "MAC");
    os.put("version", "10.7.4");

    JSONObject json = new JSONObject();
    json.put("build", build);
    json.put("os", os);

    // TODO ddary find a solution for non existing session
    WebDriverLikeResponse r = new WebDriverLikeResponse("0815", 200, json);


    return r;

  }
}
