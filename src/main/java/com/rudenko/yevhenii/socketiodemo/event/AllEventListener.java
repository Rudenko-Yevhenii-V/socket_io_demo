package com.rudenko.yevhenii.socketiodemo.event;

public interface AllEventListener {

	EventListener onChatEvent();

	EventListener onNotificationEvent();

	EventListener onResendEvent();

}
