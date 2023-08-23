package com.hadhemi.Task_Management.config;

import com.hadhemi.Task_Management.config.MyWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/websocket-path").setAllowedOrigins("http://localhost:4200");
    }

    @Bean
    public MyWebSocketHandler myWebSocketHandler() {
        return new MyWebSocketHandler();
    }
}
