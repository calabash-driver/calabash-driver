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
package sh.calaba.driver.client;

import sh.calaba.driver.net.WebDriverLikeCommand;

/**
 * Describes the list of supported Calabash commands with corresponding description.
 * 
 * @author ddary
 */
public enum CalabashCommands {
  CLEAR_NAMED_FIELD("clear_named_field", "", WebDriverLikeCommand.TEXT_FIELD),
    CLEAR_NUMBERED_FIELD("clear_numbered_field", "", WebDriverLikeCommand.TEXT_FIELD),
  CLICK_ON_TEXT("click_on_text", "", WebDriverLikeCommand.VIEW),
  CLICK_ON_VIEW_BY_ID("click_on_view_by_id", "", WebDriverLikeCommand.VIEW),
  CLEAR_ID_FIELD("clear_id_field","",WebDriverLikeCommand.TEXT_FIELD),
  ENTER_QUERY_INTO_NUMBERED_FIELD("enter_query_into_numbered_field", "", WebDriverLikeCommand.TEXT_FIELD),
  ENTER_TEXT_INTO_ID_FIELD("enter_text_into_id_field", "", WebDriverLikeCommand.TEXT_FIELD),
  ENTER_TEXT_INTO_NAMED_FIELD("enter_text_into_named_field", "", WebDriverLikeCommand.TEXT_FIELD),
  ENTER_TEXT_INTO_NUMBERED_FIELD("enter_text_into_numbered_field", "",WebDriverLikeCommand.TEXT_FIELD),
  GET_TEXT_BY_ID("get_text_by_id", "", WebDriverLikeCommand.VIEW),
  GET_VIEW_PROPERTY("get_view_property","",WebDriverLikeCommand.VIEW),
  GO_BACK("go_back","",WebDriverLikeCommand.DEVICE),
  LONG_PRESS_LIST_ITEM("long_press_list_item", "", WebDriverLikeCommand.LIST_ITEM),
  PRESS("press", "Press an ui view", WebDriverLikeCommand.VIEW),
  PRESS_BUTTON_NUMBER("press_button_number", "", WebDriverLikeCommand.BUTTON),
  PRESS_L10N_ELEMENT("press_l10n_element", "", WebDriverLikeCommand.VIEW),
  PRESS_LIST_ITEM("press_list_item", "", WebDriverLikeCommand.LIST_ITEM),
  PRESS_LONG_ON_TEXT("press_long_on_text'", "", WebDriverLikeCommand.VIEW),
  SCROLL_DOWN("scroll_down", "", WebDriverLikeCommand.DEVICE),
  SCROLL_UP("scroll_up", "", WebDriverLikeCommand.DEVICE),
  SELECT_ITEM_FROM_NAMED_SPINNER("select_item_from_named_spinner", "", WebDriverLikeCommand.SPINNER),
  SEND_KEY_ENTER("send_key_enter","",WebDriverLikeCommand.DEVICE),
  SET_SET("set_text", "set text on a webview", WebDriverLikeCommand.WEB_VIEW),
  SWIPE("swipe","",WebDriverLikeCommand.DEVICE),
  TAKE_SCREENSHOT("take_screenshot_embed", "", WebDriverLikeCommand.DEVICE),
  TOUCH("touch", "touch something on a webview", WebDriverLikeCommand.WEB_VIEW),
  WAIT("wait", "", WebDriverLikeCommand.VIEW),
  WAIT_FOR_DIALOG_TO_CLOSE("wait_for_dialog_to_close", "", WebDriverLikeCommand.WAIT),
  WAIT_FOR_L10N_ELEMENT("wait_for_l10n_element", "", WebDriverLikeCommand.VIEW),
  WAIT_FOR_NO_PROGRESS_BARS("wait_for_no_progress_bars", "", WebDriverLikeCommand.WAIT),
  WAIT_FOR_TEXT("wait_for_text", "", WebDriverLikeCommand.WAIT),
  WAIT_FOR_VIEW_BY_ID("wait_for_view_by_id", "", WebDriverLikeCommand.VIEW);

  String desc;
  String name;
  WebDriverLikeCommand webDriverCommand;

  CalabashCommands(String name, String desc, WebDriverLikeCommand webDriverCommand) {
    this.desc = desc;
    this.name = name;
    this.webDriverCommand = webDriverCommand;
  }

  public String getCommand() {
    return name;
  }

  public WebDriverLikeCommand getWebDriverLikeCommand() {
    return webDriverCommand;
  }
}
