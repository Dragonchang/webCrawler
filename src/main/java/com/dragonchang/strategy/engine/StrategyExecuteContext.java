package com.dragonchang.strategy.engine;

import com.alibaba.fastjson.JSONObject;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.domain.po.StrategyInfo;
import com.dragonchang.domain.po.StrategyVersion;
import com.dragonchang.service.StrategyRunPushService;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StrategyExecuteContext {
    private Long runId;
    private Long strategyId;
    private StrategyInfo strategyInfo;
    private StrategyVersion strategyVersion;
    private JSONObject params;
    private String dataSnapshotDate;
    private List<CompanyStock> universe;
    private Map<Integer, FinanceAnalysis> financeMap;
    private StrategyRunPushService pushService;
}
