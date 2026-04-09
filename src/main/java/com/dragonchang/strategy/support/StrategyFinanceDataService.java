package com.dragonchang.strategy.support;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.mapper.FinanceAnalysisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StrategyFinanceDataService {

    @Autowired
    private FinanceAnalysisMapper financeAnalysisMapper;

    public FinanceAnalysis latestFinance(Integer stockId) {
        return financeAnalysisMapper.selectOne(new LambdaQueryWrapper<FinanceAnalysis>()
                .eq(FinanceAnalysis::getStockCompanyId, stockId)
                .orderByDesc(FinanceAnalysis::getReportTime)
                .last("limit 1"));
    }

    public Map<Integer, FinanceAnalysis> latestFinanceMap(List<Integer> stockIds) {
        if (stockIds == null || stockIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<FinanceAnalysis> list = financeAnalysisMapper.selectList(new LambdaQueryWrapper<FinanceAnalysis>()
                .in(FinanceAnalysis::getStockCompanyId, stockIds)
                .eq(FinanceAnalysis::getDeleted, 0)
                .orderByAsc(FinanceAnalysis::getStockCompanyId)
                .orderByDesc(FinanceAnalysis::getReportTime)
                .orderByDesc(FinanceAnalysis::getId));
        Map<Integer, FinanceAnalysis> result = new LinkedHashMap<Integer, FinanceAnalysis>();
        for (FinanceAnalysis item : list) {
            if (!result.containsKey(item.getStockCompanyId())) {
                result.put(item.getStockCompanyId(), item);
            }
        }
        return result;
    }
}

