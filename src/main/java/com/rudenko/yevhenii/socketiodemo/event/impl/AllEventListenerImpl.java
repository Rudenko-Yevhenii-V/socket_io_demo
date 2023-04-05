package com.rudenko.yevhenii.socketiodemo.event.impl;

import com.rudenko.yevhenii.socketiodemo.dto.mapper.SocketIoMapper;
import com.rudenko.yevhenii.socketiodemo.dto.request.AcknowledgmentDto;
import com.rudenko.yevhenii.socketiodemo.dto.request.PayloadDto;
import com.rudenko.yevhenii.socketiodemo.dto.response.ChatEventDto;
import com.rudenko.yevhenii.socketiodemo.dto.response.NotificationDto;
import com.rudenko.yevhenii.socketiodemo.dto.response.ResendDto;
import com.rudenko.yevhenii.socketiodemo.event.AllEventListener;
import com.rudenko.yevhenii.socketiodemo.event.EventListener;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.rudenko.yevhenii.socketiodemo.event.EventConstants.*;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AllEventListenerImpl implements AllEventListener {

	SocketIoMapper socketIoMapper;


	public EventListener onChatEvent() {
		return (socket, args) -> {

			AcknowledgmentDto<ChatEventDto> dto = socketIoMapper.toAcknowledgementDto(ChatEventDto.class, args);
			ChatEventDto data = dto.getData();

			// request - response
			Optional.ofNullable(dto.getAcknowledgment()).ifPresent(ack ->
					ack.sendAcknowledgement("Message: '" + data.getMessage() + "' received!"));

			// send  only JSONObject, JSONArray, string/null;
			// only for one socket
//			socket.send(CHAT_EVENT, new JSONObject(ChatEventDto.builder()
//					.userName(data.getUserName())
//					.message("Only you see this message, shhh...")
//					.build()));

			// for public room
			socket.getNamespace().broadcast(CHAT_ROOM, CHAT_EVENT, new JSONObject(data));

			log.info("ChatEvent from: {}, message: {}", data.getUserName(), data.getMessage());
		};
	}


	public EventListener onNotificationEvent() {
		return (socket, args) -> {

			PayloadDto<NotificationDto> dto = socketIoMapper.toPayloadDto(NotificationDto.class, args);

			NotificationDto data = dto.getData();
			String user = getAuthUser();
			log.info("Authed user: {}", user);

			// only for one user (different devices)
			socket.getNamespace().broadcast(user, NOTIFICATION_EVENT, new JSONObject(data));

			log.info("NotificationEvent from: {}, message: {}", user, data.getNotification());
		};
	}


	public EventListener onResendEvent() {
		return (socket, args) -> {

			PayloadDto<ResendDto> ack = socketIoMapper.toPayloadDto(ResendDto.class, args);

			ResendDto data = ack.getData();
			socket.getNamespace().broadcast(data.getRoom(), RESEND_EVENT, data.getMessage());

			log.info("ResendEvent for room: {}, message: {}", data.getRoom(), data.getMessage());
		};
	}


	private String getAuthUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (String) authentication.getPrincipal();
	}

}
