package sh.calaba.driver.client;

import sh.calaba.driver.net.WebDriverLikeCommand;

public enum CalabashCommands {
	PRESS_LIST_ITEM("press_list_item","",WebDriverLikeCommand.LIST_ITEM),
	LONG_PRESS_LIST_ITEM("long_press_list_item","",WebDriverLikeCommand.LIST_ITEM),
	PRESS_LIST_ITEM_TEXT("press_list_item_text","",WebDriverLikeCommand.LIST_ITEM),
	PRESS_BUTTON_NUMBER("press_button_number","",WebDriverLikeCommand.BUTTON),
	PRESS_BUTTON_WITH_TEXT("press_button_with_text","",WebDriverLikeCommand.BUTTON),
	ENTER_TEXT_INTO_NUMBERED_FIELD("enter_text_into_numbered_field","",WebDriverLikeCommand.TEXT_FIELD),
    ENTER_TEXT_INTO_NAMED_FIELD("enter_text_into_named_field","",WebDriverLikeCommand.TEXT_FIELD),
	CLEAR_NUMBERED_FIELD("clear_numbered_field","",WebDriverLikeCommand.TEXT_FIELD),
    CLEAR_NAMED_FIELD("clear_named_field","",WebDriverLikeCommand.TEXT_FIELD),
	CLICK_ON_VIEW_BY_ID("click_on_view_by_id","",WebDriverLikeCommand.VIEW),
	CLICK_ON_TEXT("click_on_text","",WebDriverLikeCommand.VIEW),
	LONG_PRESS_ON_VIEW_BY_ID("long_press_on_view_by_id","",WebDriverLikeCommand.VIEW),
	WAIT_FOR_VIEW_BY_ID("wait_for_view_by_id","",WebDriverLikeCommand.VIEW),
	PRESS("press","Press an ui view",WebDriverLikeCommand.VIEW),
	WAIT_FOR_L10N_ELEMENT("wait_for_l10n_element","",WebDriverLikeCommand.VIEW),
	PRESS_L10N_ELEMENT("press_l10n_element","",WebDriverLikeCommand.VIEW),
	SCROLL_UP("scroll_up","",WebDriverLikeCommand.VIEW),
    SCROLL_DOWN("scroll_down","",WebDriverLikeCommand.VIEW),
	//TAKE_SCREENSHOT("take_screenshot_robotium","",WebDriverLikeCommand.VIEW),
	
	WAIT_FOR_DIALOG_TO_CLOSE("wait_for_dialog_to_close","",WebDriverLikeCommand.WAIT),
	WAIT_FOR_TEXT("wait_for_text","",WebDriverLikeCommand.WAIT),
	WAIT_FOR_BUTTON("wait_for_button","",WebDriverLikeCommand.WAIT),
	WAIT_FOR_NO_PROGRESS_BARS("wait_for_no_progress_bars","",WebDriverLikeCommand.WAIT);
	
	String name;
	String desc;
	WebDriverLikeCommand webDriverCommand;
	CalabashCommands(String name,String desc,WebDriverLikeCommand webDriverCommand){
		this.desc=desc;
		this.name=name;
		this.webDriverCommand=webDriverCommand;
	}
	
	public String getCommand(){
		return name;
	}
	
	public WebDriverLikeCommand getWebDriverLikeCommand(){
		return webDriverCommand;
	}

}
