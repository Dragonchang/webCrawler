package com.dragonchang.strategy.engine;

public interface StrategyExecutor {
    String getScriptType();

    StrategyExecuteResult execute(StrategyExecuteContext context);
}

