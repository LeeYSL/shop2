package config;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import websocket.echoHandler;

@Configuration
@EnableWebSocket //웹소켓 처리
public class WebSocketConfig implements WebSocketConfigurer {
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry regosrty) {
		regosrty.addHandler(new echoHandler(), "chatting")
		.setAllowedOrigins("*"); //모든 브라우저 환경
		
	}

}
