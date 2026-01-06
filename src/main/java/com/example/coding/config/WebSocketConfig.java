package com.example.coding.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * WebSocket配置类
 * 配置WebSocket端点和消息处理器
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final UnlockWebSocketHandler unlockWebSocketHandler;

    @Autowired
    public WebSocketConfig(UnlockWebSocketHandler unlockWebSocketHandler) {
        this.unlockWebSocketHandler = unlockWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册WebSocket处理器，允许跨域访问
        registry.addHandler(unlockWebSocketHandler, "/ws/unlock")
                .setAllowedOrigins("*");
    }
}
