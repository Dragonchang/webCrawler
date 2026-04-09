package com.dragonchang.strategy.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StrategyExecutorFactory {

    @Autowired
    private List<StrategyExecutor> executors;

    public StrategyExecutor getExecutor(String scriptType) {
        for (StrategyExecutor executor : executors) {
            if (executor.getScriptType().equalsIgnoreCase(scriptType)) {
                return executor;
            }
        }
        throw new IllegalArgumentException("未找到执行器: " + scriptType);
    }
}

