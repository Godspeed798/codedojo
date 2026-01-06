package com.example.coding.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket消息处理器
 * 处理关卡解锁的实时通知
 */
@Component
public class UnlockWebSocketHandler extends TextWebSocketHandler {

    // 存储用户ID到WebSocket会话的映射（支持多设备同时在线）
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从URL参数中获取userId
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.put(userId, session);
            System.out.println("WebSocket连接建立: userId=" + userId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 移除断开的连接
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.remove(userId);
            System.out.println("WebSocket连接断开: userId=" + userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理客户端发送的消息（如心跳包）
        String payload = message.getPayload();
        if ("ping".equals(payload)) {
            session.sendMessage(new TextMessage("pong"));
        }
    }

    /**
     * 向指定用户发送解锁通知
     * @param userId 用户ID
     * @param unlockedContent 解锁的内容类型（sorting, search, all）
     */
    public void notifyUnlock(String userId, String unlockedContent) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                Map<String, Object> message = Map.of(
                    "type", "unlock",
                    "content", unlockedContent,
                    "timestamp", System.currentTimeMillis()
                );
                String jsonMessage = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(jsonMessage));
                System.out.println("发送解锁通知: userId=" + userId + ", content=" + unlockedContent);
            } catch (IOException e) {
                System.err.println("发送WebSocket消息失败: " + e.getMessage());
            }
        } else {
            System.out.println("用户未在线或连接已关闭: userId=" + userId);
        }
    }

    /**
     * 从WebSocket会话中获取用户ID
     */
    private String getUserIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("userId=")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("userId=")) {
                    return param.substring(7); // 去掉"userId="前缀
                }
            }
        }
        return null;
    }

    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(String userId) {
        WebSocketSession session = userSessions.get(userId);
        return session != null && session.isOpen();
    }
}
