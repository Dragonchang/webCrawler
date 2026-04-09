package com.dragonchang.config;

import com.dragonchang.websocket.StrategyRunWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class StrategyWebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private StrategyRunWebSocketHandler strategyRunWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(strategyRunWebSocketHandler, "/ws/strategyRun").setAllowedOrigins("*");
    }
}

