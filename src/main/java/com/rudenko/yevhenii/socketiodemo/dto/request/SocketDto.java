package com.rudenko.yevhenii.socketiodemo.dto.request;

import io.socket.socketio.server.SocketIoSocket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = PRIVATE)
public class SocketDto {

	SocketIoSocket socket;

}
