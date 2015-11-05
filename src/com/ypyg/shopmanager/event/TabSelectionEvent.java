package com.ypyg.shopmanager.event;

//具体要打开第几个tab的页面
public class TabSelectionEvent {
	private int tabPosition;

	public int getTabPosition() {
		return tabPosition;
	}

	public void setTabPosition(int tabPosition) {
		this.tabPosition = tabPosition;
	}

}
