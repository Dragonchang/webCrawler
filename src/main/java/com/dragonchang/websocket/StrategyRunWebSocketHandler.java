package com.dragonchang.websocket;

import com.dragonchang.service.StrategyRunPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class StrategyRunWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private StrategyRunPushService strategyRunPushService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        strategyRunPushService.register(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        strategyRunPushService.handleClientMessage(session, message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        strategyRunPushService.unregister(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        strategyRunPushService.unregister(session);
    }
}

