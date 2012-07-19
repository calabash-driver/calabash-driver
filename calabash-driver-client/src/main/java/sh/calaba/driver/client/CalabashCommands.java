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
  PRESS_LIST_ITEM("press_list_item", "", WebDriverLikeCommand.LIST_ITEM), LONG_PRESS_LIST_ITEM(
      "long_press_list_item", "", WebDriverLikeCommand.LIST_ITEM), PRESS_LIST_ITEM_TEXT(
      "press_list_item_text", "", WebDriverLikeCommand.LIST_ITEM), PRESS_BUTTON_NUMBER(
      "press_button_number", "", WebDriverLikeCommand.BUTTON), PRESS_BUTTON_WITH_TEXT(
      "press_button_with_text", "", WebDriverLikeCommand.BUTTON), ENTER_TEXT_INTO_NUMBERED_FIELD(
      "enter_text_into_numbered_field", "", WebDriverLikeCommand.TEXT_FIELD), ENTER_TEXT_INTO_NAMED_FIELD(
      "enter_text_into_named_field", "", WebDriverLikeCommand.TEXT_FIELD), CLEAR_NUMBERED_FIELD(
      "clear_numbered_field", "", WebDriverLikeCommand.TEXT_FIELD), CLEAR_NAMED_FIELD(
      "clear_named_field", "", WebDriverLikeCommand.TEXT_FIELD), CLICK_ON_VIEW_BY_ID(
      "click_on_view_by_id", "", WebDriverLikeCommand.VIEW), CLICK_ON_TEXT("click_on_text", "",
      WebDriverLikeCommand.VIEW), LONG_PRESS_ON_VIEW_BY_ID("long_press_on_view_by_id", "",
      WebDriverLikeCommand.VIEW), WAIT_FOR_VIEW_BY_ID("wait_for_view_by_id", "",
      WebDriverLikeCommand.VIEW), PRESS("press", "Press an ui view", WebDriverLikeCommand.VIEW), WAIT_FOR_L10N_ELEMENT(
      "wait_for_l10n_element", "", WebDriverLikeCommand.VIEW), PRESS_L10N_ELEMENT(
      "press_l10n_element", "", WebDriverLikeCommand.VIEW), SCROLL_UP("scroll_up", "",
      WebDriverLikeCommand.VIEW), SCROLL_DOWN("scroll_down", "", WebDriverLikeCommand.VIEW), SEARCH_FOR(
      "search_for", "", WebDriverLikeCommand.VIEW), TAKE_SCREENSHOT("take_screenshot_robotium", "",
      WebDriverLikeCommand.VIEW), SELECT_ITEM_FROM_NAMED_SPINNER("select_item_from_named_spinner",
      "", WebDriverLikeCommand.SPINNER), SELECT_ITEM_BY_NAME("select_item_by_name", "",
      WebDriverLikeCommand.SPINNER), CLICK_ON_VIEW_BY_NAME("click_on_view_by_name", "",
      WebDriverLikeCommand.VIEW), PRESS_LONG_ON_TEXT("press_long_on_text'", "",
      WebDriverLikeCommand.VIEW), WAIT_FOR_VIEW_BY_NAME("wait_for_view_by_name", "",
      WebDriverLikeCommand.VIEW), ENTER_TEXT_BY_NAME("enter_text_by_name", "",
      WebDriverLikeCommand.TEXT_FIELD), GET_ELEMENT_TEXT_BY_TYPE("get_element_text_by_type", "",
      WebDriverLikeCommand.VIEW), GET_ELEMENT_TEXT_BY_NAME("get_element_text_by_name", "",
      WebDriverLikeCommand.VIEW), VIEW_ENABLED_STATUS_BY_NAME("view_enabled_status_by_name", "",
      WebDriverLikeCommand.VIEW), WAIT_FOR_DIALOG_TO_CLOSE("wait_for_dialog_to_close", "",
      WebDriverLikeCommand.WAIT), SET_SET("set_text", "set text on a webview",
      WebDriverLikeCommand.WEB_VIEW), TOUCH("touch", "touch something on a webview",
      WebDriverLikeCommand.WEB_VIEW),

  WAIT("wait", "", WebDriverLikeCommand.VIEW), WAIT_FOR_TEXT("wait_for_text", "",
      WebDriverLikeCommand.WAIT), WAIT_FOR_BUTTON("wait_for_button", "", WebDriverLikeCommand.WAIT), WAIT_FOR_NO_PROGRESS_BARS(
      "wait_for_no_progress_bars", "", WebDriverLikeCommand.WAIT);

  String name;
  String desc;
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
