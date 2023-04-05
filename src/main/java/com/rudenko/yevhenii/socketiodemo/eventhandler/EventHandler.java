package com.rudenko.yevhenii.socketiodemo.eventhandler;

import com.rudenko.yevhenii.socketiodemo.dto.mapper.SocketIoMapper;
import com.rudenko.yevhenii.socketiodemo.dto.request.SocketDto;
import com.rudenko.yevhenii.socketiodemo.event.EventListener;
import com.rudenko.yevhenii.socketiodemo.event.EventRegistrar;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static com.rudenko.yevhenii.socketiodemo.event.EventConstants.CHAT_ROOM;
import static com.rudenko.yevhenii.socketiodemo.event.EventConstants.CONNECTION_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class EventHandler {

	SocketIoServer socketIoServer;
	EventRegistrar eventRegistrar;
	SocketIoMapper socketIoMapper;

	@PostConstruct
	public void handleEvents() {
		socketIoServer.namespace("/").on(CONNECTION_EVENT, args -> {

			SocketDto socketDto = socketIoMapper.toSocketDto(args);
			SocketIoSocket socket = socketDto.getSocket();

			joinUserRoom(socket);

			socket.registerAllEventListener((eventName, data) -> {
				authenticate(socket);
				log.info("New event: {}", eventName);

				EventListener eventListener = eventRegistrar.getEventListeners().get(eventName);

				Optional.ofNullable(eventListener)
						.ifPresent(listener -> listener.onEvent(socket, data));
			});

		});
	}


	private void joinUserRoom(SocketIoSocket socket) {
		Optional<String> authorization = authenticate(socket);
		log.info("Authed user id: {}", authorization);

		authorization.ifPresentOrElse(
				socket::joinRoom,
				() -> socket.disconnect(true));

		socket.joinRoom(CHAT_ROOM);
	}

	private Optional<String> authenticate(SocketIoSocket socket) {
		// possible socket.getConnectData() -> { "token": "..." }
		Optional<String> authorization = socket.getInitialHeaders()
				.get("authorization")
				.stream().findAny();

		authorization.ifPresent(this::addUserToSecurityContext);

		return authorization;
	}

	private void addUserToSecurityContext(String username) {
		SecurityContextHolder.getContext()
				.setAuthentication(
						new UsernamePasswordAuthenticationToken(username, "", List.of())
				);
	}

}
