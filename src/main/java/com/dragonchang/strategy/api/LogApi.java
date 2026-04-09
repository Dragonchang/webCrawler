package com.dragonchang.strategy.api;

import com.dragonchang.domain.po.StrategyRunLog;
import com.dragonchang.service.StrategyRunPushService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogApi {
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Long runId;
    private final Long strategyId;
    private final StrategyRunPushService strategyRunPushService;
    private int lineNo = 1;
    private final List<StrategyRunLog> logs = new ArrayList<StrategyRunLog>();

    public LogApi(Long runId, Long strategyId, StrategyRunPushService strategyRunPushService) {
        this.runId = runId;
        this.strategyId = strategyId;
        this.strategyRunPushService = strategyRunPushService;
    }

    public void info(String message) {
        add("INFO", message);
    }

    public void warn(String message) {
        add("WARN", message);
    }

    public void error(String message) {
        add("ERROR", message);
    }

    private void add(String level, String message) {
        LocalDateTime now = LocalDateTime.now();
        StrategyRunLog log = new StrategyRunLog();
        log.setRunId(runId);
        log.setLineNo(lineNo++);
        log.setLogLevel(level);
        log.setContent(message);
        log.setLogTime(now);
        logs.add(log);
        if (strategyRunPushService != null) {
            strategyRunPushService.pushLog(runId, strategyId, level, now.format(DF), message);
        }
    }

    public List<StrategyRunLog> getLogs() {
        return logs;
    }
}
