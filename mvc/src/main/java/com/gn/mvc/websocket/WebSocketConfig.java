package com.gn.mvc.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
	
	private final BasicWebSocketHandler basicWebSocketHandler;
	private final ChatWebSocketHandler chatwebSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(basicWebSocketHandler, "/ws/basic")
				.setAllowedOrigins("http://localhost:8080");
		
		registry.addHandler(chatwebSocketHandler, "/ws/chat")
				.setAllowedOrigins("http://localhost:8080");
		
				
		
	}
	
}
