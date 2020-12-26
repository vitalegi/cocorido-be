package it.vitalegi.cocorido;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import it.vitalegi.cocorido.util.ListUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Value("${cors.allowedOrigins}")
	List<String> corsAllowedOrigins;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		log.info("Allowed origins: {}", corsAllowedOrigins);
		registry.addEndpoint("/chat")//
				.setAllowedOrigins("http://localhost:8081")//
				.addInterceptors(new HandshakeInterceptor() {

					@Override
					public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
							WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
						log.info("beforeHandshake");
						return true;
					}

					@Override
					public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
							WebSocketHandler wsHandler, Exception exception) {
						log.info("afterHandshake {} - {}", exception, response);
					}
				}).withSockJS();
	}
}
