package com.ypyg.shopmanager.event;

import de.greenrobot.event.EventBus;

public class BusProvider {

	private BusProvider() {
	}

	public static EventBus get() {
		return EventBus.getDefault();
	}
}
