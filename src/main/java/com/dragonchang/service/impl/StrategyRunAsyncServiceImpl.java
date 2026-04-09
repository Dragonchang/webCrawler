package com.dragonchang.service.impl;

import com.dragonchang.service.IStrategyRunService;
import com.dragonchang.service.StrategyRunAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StrategyRunAsyncServiceImpl implements StrategyRunAsyncService {

    @Autowired
    private IStrategyRunService strategyRunService;

    @Override
    @Async("strategyRunTaskExecutor")
    public void executeAsync(Long runId) {
        log.info("submit async strategy run, runId={}", runId);
        strategyRunService.executeCreatedRun(runId);
    }
}
