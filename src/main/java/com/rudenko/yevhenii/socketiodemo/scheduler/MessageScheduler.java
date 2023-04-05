package com.rudenko.yevhenii.socketiodemo.scheduler;

import io.socket.socketio.server.SocketIoServer;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class MessageScheduler {

	SocketIoServer socketIoServer;

//	@Scheduled(fixedDelay = 10000)
//	public void sendMessageToChat() {
//		// room can bee replaced by userId
//		socketIoServer.namespace("/").broadcast(CHAT_ROOM, CHAT_EVENT,
//				new JSONObject(ChatEventDto.builder()
//						.message("Scheduled message!!!")
//						.userName("SystemUser")
//						.build()));
//	}

}
