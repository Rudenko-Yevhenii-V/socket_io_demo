package com.rudenko.yevhenii.socketiodemo.event.impl;

import com.rudenko.yevhenii.socketiodemo.event.AllEventListener;
import com.rudenko.yevhenii.socketiodemo.event.EventListener;
import com.rudenko.yevhenii.socketiodemo.event.EventRegistrar;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.rudenko.yevhenii.socketiodemo.event.EventConstants.*;
import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class EventRegistrarImpl implements EventRegistrar {

	// unmodifiableMap
	private static Map<String, EventListener> eventListeners = new HashMap<>();

	AllEventListener allEventListener;

	@PostConstruct
	public void mapListeners() {
		eventListeners.put(CHAT_EVENT, allEventListener.onChatEvent());
		eventListeners.put(NOTIFICATION_EVENT, allEventListener.onNotificationEvent());
		eventListeners.put(RESEND_EVENT, allEventListener.onResendEvent());

		eventListeners = Collections.unmodifiableMap(eventListeners);
	}


	@Override
	public Map<String, EventListener> getEventListeners() {
		return eventListeners;
	}

}
