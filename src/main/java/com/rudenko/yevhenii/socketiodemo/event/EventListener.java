package com.rudenko.yevhenii.socketiodemo.event;

import io.socket.socketio.server.SocketIoSocket;

@FunctionalInterface
public interface EventListener {

	void onEvent(SocketIoSocket socket, Object... args);

}
