package com.rudenko.yevhenii.socketiodemo.event;

import java.util.Map;

public interface EventRegistrar {

	Map<String, EventListener> getEventListeners();

}
