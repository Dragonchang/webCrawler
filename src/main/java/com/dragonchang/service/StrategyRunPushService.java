package com.dragonchang.service;

import com.dragonchang.domain.vo.StrategyRunPushMessageVO;
import org.springframework.web.socket.WebSocketSession;

public interface StrategyRunPushService {
    void register(WebSocketSession session);

    void unregister(WebSocketSession session);

    void handleClientMessage(WebSocketSession session, String payload);

    void pushProgress(Long runId, Long strategyId, String status, Integer progress, String stageCode, String message);

    void pushStepProgress(Long runId, Long strategyId, String status, Integer progress, String stageCode,
                          Integer current, Integer total, String message);

    void pushLog(Long runId, Long strategyId, String logLevel, String logTime, String message);

    void pushCompleted(Long runId, Long strategyId, String status, Integer progress, Integer resultCount, String message);

    StrategyRunPushMessageVO getSnapshot(Long runId);
}
