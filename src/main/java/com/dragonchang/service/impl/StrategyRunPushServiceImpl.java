package com.dragonchang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dragonchang.domain.vo.StrategyRunPushMessageVO;
import com.dragonchang.service.StrategyRunPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class StrategyRunPushServiceImpl implements StrategyRunPushService {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Map<Long, Set<WebSocketSession>> runSessionMap = new ConcurrentHashMap<Long, Set<WebSocketSession>>();
    private final Map<Long, Set<WebSocketSession>> strategySessionMap = new ConcurrentHashMap<Long, Set<WebSocketSession>>();
    private final Map<String, Long> sessionRunMap = new ConcurrentHashMap<String, Long>();
    private final Map<String, Long> sessionStrategyMap = new ConcurrentHashMap<String, Long>();
    private final Map<Long, StrategyRunPushMessageVO> snapshotMap = new ConcurrentHashMap<Long, StrategyRunPushMessageVO>();

    @Override
    public void register(WebSocketSession session) {
        log.info("strategy websocket connected, sessionId={}", session.getId());
    }

    @Override
    public void unregister(WebSocketSession session) {
        Long runId = sessionRunMap.remove(session.getId());
        if (runId != null) {
            Set<WebSocketSession> sessions = runSessionMap.get(runId);
            if (sessions != null) {
                sessions.remove(session);
            }
        }
        Long strategyId = sessionStrategyMap.remove(session.getId());
        if (strategyId != null) {
            Set<WebSocketSession> sessions = strategySessionMap.get(strategyId);
            if (sessions != null) {
                sessions.remove(session);
            }
        }
        log.info("strategy websocket disconnected, sessionId={}, runId={}, strategyId={}", session.getId(), runId, strategyId);
    }

    @Override
    public void handleClientMessage(WebSocketSession session, String payload) {
        try {
            JSONObject json = JSON.parseObject(payload);
            Long runId = json.getLong("runId");
            Long strategyId = json.getLong("strategyId");
            if (runId != null) {
                sessionRunMap.put(session.getId(), runId);
                runSessionMap.computeIfAbsent(runId, key -> ConcurrentHashMap.newKeySet()).add(session);
                StrategyRunPushMessageVO snapshot = snapshotMap.get(runId);
                if (snapshot != null) {
                    send(session, snapshot);
                }
            }
            if (strategyId != null) {
                sessionStrategyMap.put(session.getId(), strategyId);
                strategySessionMap.computeIfAbsent(strategyId, key -> ConcurrentHashMap.newKeySet()).add(session);
            }
            log.info("strategy websocket subscribed, sessionId={}, runId={}, strategyId={}", session.getId(), runId, strategyId);
        } catch (Exception e) {
            log.error("handle websocket message error, payload={}", payload, e);
        }
    }

    @Override
    public void pushProgress(Long runId, Long strategyId, String status, Integer progress, String stageCode, String message) {
        StrategyRunPushMessageVO push = new StrategyRunPushMessageVO();
        push.setRunId(runId);
        push.setStrategyId(strategyId);
        push.setEventType("PROGRESS");
        push.setStatus(status);
        push.setProgress(progress);
        push.setStageCode(stageCode);
        push.setMessage(message);
        snapshotMap.put(runId, push);
        broadcast(runId, strategyId, push);
    }

    @Override
    public void pushStepProgress(Long runId, Long strategyId, String status, Integer progress, String stageCode,
                                 Integer current, Integer total, String message) {
        StrategyRunPushMessageVO push = new StrategyRunPushMessageVO();
        push.setRunId(runId);
        push.setStrategyId(strategyId);
        push.setEventType("STEP_PROGRESS");
        push.setStatus(status);
        push.setProgress(progress);
        push.setStageCode(stageCode);
        push.setCurrent(current);
        push.setTotal(total);
        push.setMessage(message);
        snapshotMap.put(runId, push);
        broadcast(runId, strategyId, push);
    }

    @Override
    public void pushLog(Long runId, Long strategyId, String logLevel, String logTime, String message) {
        StrategyRunPushMessageVO push = new StrategyRunPushMessageVO();
        push.setRunId(runId);
        push.setStrategyId(strategyId);
        push.setEventType("LOG");
        push.setLogLevel(logLevel);
        push.setLogTime(logTime == null ? LocalDateTime.now().format(DF) : logTime);
        push.setMessage(message);
        broadcast(runId, strategyId, push);
    }

    @Override
    public void pushCompleted(Long runId, Long strategyId, String status, Integer progress, Integer resultCount, String message) {
        StrategyRunPushMessageVO push = new StrategyRunPushMessageVO();
        push.setRunId(runId);
        push.setStrategyId(strategyId);
        push.setEventType("DONE");
        push.setStatus(status);
        push.setProgress(progress);
        push.setResultCount(resultCount);
        push.setMessage(message);
        snapshotMap.put(runId, push);
        broadcast(runId, strategyId, push);
    }

    @Override
    public StrategyRunPushMessageVO getSnapshot(Long runId) {
        return snapshotMap.get(runId);
    }

    private void broadcast(Long runId, Long strategyId, StrategyRunPushMessageVO push) {
        broadcastRun(runId, push);
        broadcastStrategy(strategyId, push);
    }

    private void broadcastRun(Long runId, StrategyRunPushMessageVO push) {
        if (runId == null) {
            return;
        }
        Set<WebSocketSession> sessions = runSessionMap.get(runId);
        if (sessions == null || sessions.isEmpty()) {
            return;
        }
        for (WebSocketSession session : sessions) {
            send(session, push);
        }
    }

    private void broadcastStrategy(Long strategyId, StrategyRunPushMessageVO push) {
        if (strategyId == null) {
            return;
        }
        Set<WebSocketSession> sessions = strategySessionMap.get(strategyId);
        if (sessions == null || sessions.isEmpty()) {
            return;
        }
        for (WebSocketSession session : sessions) {
            send(session, push);
        }
    }

    private void send(WebSocketSession session, StrategyRunPushMessageVO push) {
        if (session == null || !session.isOpen()) {
            return;
        }
        try {
            session.sendMessage(new TextMessage(JSON.toJSONString(push)));
        } catch (IOException e) {
            log.error("send websocket message error, sessionId={}", session.getId(), e);
        }
    }
}
