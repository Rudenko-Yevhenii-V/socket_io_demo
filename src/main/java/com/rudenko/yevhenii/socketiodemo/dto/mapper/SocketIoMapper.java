package com.rudenko.yevhenii.socketiodemo.dto.mapper;

import com.rudenko.yevhenii.socketiodemo.dto.request.AcknowledgmentDto;
import com.rudenko.yevhenii.socketiodemo.dto.request.PayloadDto;
import com.rudenko.yevhenii.socketiodemo.dto.request.SocketDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.socket.socketio.server.SocketIoSocket;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class SocketIoMapper {

	ObjectMapper objectMapper;

	public SocketDto toSocketDto(Object... args) {
		if ((args.length == 1) && (args[0] instanceof SocketIoSocket)) {
			return SocketDto.builder()
					.socket((SocketIoSocket) args[0])
					.build();
		}

		return null;
	}

	public <T> PayloadDto<T> toPayloadDto(Class<T> toClass, Object... args) {
		Object data = null;

		try {
			data = toClass.cast(objectMapper.readValue(args[0].toString(), toClass));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return (PayloadDto<T>) PayloadDto.builder()
				.data(data)
				.build();
	}

	public <T> AcknowledgmentDto<T> toAcknowledgementDto(Class<T> toClass, Object... args) {
		PayloadDto<T> payloadDto = toPayloadDto(toClass, args);

		AcknowledgmentDto.AcknowledgmentDtoBuilder<Object, ?, ?> builder = AcknowledgmentDto.builder()
				.data(payloadDto.getData());

		if (args.length > 0 && (args[1] instanceof SocketIoSocket.ReceivedByLocalAcknowledgementCallback)) {
			builder.acknowledgment((SocketIoSocket.ReceivedByLocalAcknowledgementCallback) args[1]);
		}

		return (AcknowledgmentDto<T>) builder.build();
	}

}
