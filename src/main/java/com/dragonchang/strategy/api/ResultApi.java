package com.dragonchang.strategy.api;

import com.alibaba.fastjson.JSON;
import com.dragonchang.domain.po.StrategyResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultApi {
    private final Long runId;
    private final List<StrategyResult> results = new ArrayList<>();

    public ResultApi(Long runId) {
        this.runId = runId;
    }

    public void add(Map<String, Object> item) {
        StrategyResult result = new StrategyResult();
        result.setRunId(runId);
        if (item.get("stockId") != null) {
            result.setStockId(Integer.valueOf(String.valueOf(item.get("stockId"))));
        }
        result.setStockCode(stringValue(item.get("stockCode")));
        result.setStockName(stringValue(item.get("stockName")));
        result.setActionType(stringValue(item.get("actionType")));
        result.setScore(toBigDecimal(item.get("score")));
        result.setReason(stringValue(item.get("reason")));
        Object factor = item.get("factorDetail");
        if (factor instanceof String) {
            result.setFactorDetail((String) factor);
        } else if (factor != null) {
            result.setFactorDetail(JSON.toJSONString(factor));
        }
        if (item.get("rankNo") != null) {
            result.setRankNo(Integer.valueOf(String.valueOf(item.get("rankNo"))));
        }
        result.setCreatedTime(LocalDateTime.now());
        results.add(result);
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return new BigDecimal(String.valueOf(value));
    }

    public List<StrategyResult> getResults() {
        return results;
    }
}

