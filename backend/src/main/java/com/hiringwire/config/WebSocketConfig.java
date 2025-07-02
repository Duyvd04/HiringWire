package com.hiringwire.config;

import com.hiringwire.jwt.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.logging.Logger;

@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = Logger.getLogger(WebSocketConfig.class.getName());

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    logger.info("WebSocket Authorization Header: " + authHeader);
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        logger.info("WebSocket JWT: " + token);
                        try {
                            String username = jwtHelper.getUsernameFromToken(token);
                            if (jwtHelper.validateToken(token, username)) {
                                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                        username, null, Collections.emptyList());
                                SecurityContextHolder.getContext().setAuthentication(auth);
                                accessor.setUser(auth);
                                logger.info("WebSocket Authentication Set for: " + username);
                            } else {
                                logger.info("Invalid WebSocket JWT Token");
                            }
                        } catch (Exception e) {
                            logger.info("WebSocket JWT Validation Error: " + e.getMessage());
                        }
                    } else {
                        logger.info("No Authorization header in WebSocket CONNECT");
                    }
                }
                return message;
            }
        });
    }
}