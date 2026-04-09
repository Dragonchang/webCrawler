package com.dragonchang.strategy.engine;

import com.dragonchang.domain.po.StrategyResult;
import com.dragonchang.domain.po.StrategyRunLog;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StrategyExecuteResult {
    private List<StrategyResult> results = new ArrayList<StrategyResult>();
    private List<StrategyRunLog> logs = new ArrayList<StrategyRunLog>();
    private Integer resultCount = 0;
    private Integer progress = 100;
    private String status;
    private String summary;
}
