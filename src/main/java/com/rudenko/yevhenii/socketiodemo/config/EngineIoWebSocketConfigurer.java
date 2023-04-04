package com.rudenko.yevhenii.socketiodemo.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static lombok.AccessLevel.PRIVATE;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class EngineIoWebSocketConfigurer implements WebSocketConfigurer {

    EngineIoHandler mEngineIoHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(mEngineIoHandler, "/api/v1/private/ws/")
                .addInterceptors(mEngineIoHandler)
                .setAllowedOriginPatterns("*");
    }

}