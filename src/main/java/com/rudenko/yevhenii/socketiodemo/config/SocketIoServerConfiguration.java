package com.rudenko.yevhenii.socketiodemo.config;

import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoServerOptions;
import io.socket.socketio.server.SocketIoServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SocketIoServerConfiguration {

    @Bean
    public SocketIoServer socketIoServer(EngineIoServer engineIoServer) {
        return new SocketIoServer(engineIoServer);
    }

    @Bean
    public EngineIoServer engineIoServer() {
        EngineIoServerOptions options = EngineIoServerOptions.newFromDefault();
        options.setCorsHandlingDisabled(true);

        return new EngineIoServer(options);
    }
}
