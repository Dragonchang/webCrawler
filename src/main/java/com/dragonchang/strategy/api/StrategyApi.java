package com.dragonchang.strategy.api;

import com.dragonchang.domain.po.CompanyPriceRecord;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.strategy.support.StrategyFinanceDataService;
import com.dragonchang.strategy.support.StrategyMarketDataService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StrategyApi {
    private final List<CompanyStock> universe;
    private final StrategyFinanceDataService financeDataService;
    private final StrategyMarketDataService marketDataService;
    private final Map<Integer, FinanceAnalysis> financeMap;

    public StrategyApi(List<CompanyStock> universe,
                       StrategyFinanceDataService financeDataService,
                       StrategyMarketDataService marketDataService,
                       Map<Integer, FinanceAnalysis> financeMap) {
        this.universe = universe;
        this.financeDataService = financeDataService;
        this.marketDataService = marketDataService;
        this.financeMap = financeMap;
    }

    public List<CompanyStock> stocks() {
        return universe;
    }

    public CompanyStock stock(Integer stockId) {
        for (CompanyStock stock : universe) {
            if (stockId.equals(stock.getId())) {
                return stock;
            }
        }
        return null;
    }

    public List<CompanyStock> stocksByBk(String bkName) {
        List<CompanyStock> ret = new ArrayList<CompanyStock>();
        for (CompanyStock stock : universe) {
            if (stock != null) {
                ret.add(stock);
            }
        }
        return ret;
    }

    public FinanceAnalysis finance(Integer stockId) {
        if (stockId == null) {
            return null;
        }
        FinanceAnalysis finance = financeMap == null ? null : financeMap.get(stockId);
        if (finance != null) {
            return finance;
        }
        return financeDataService.latestFinance(stockId);
    }

    public List<CompanyPriceRecord> prices(Integer stockId, Integer days) {
        return marketDataService.latestPrices(stockId, days);
    }

    public BigDecimal score(CompanyStock stock) {
        BigDecimal score = BigDecimal.ZERO;
        if (stock.getTotalCapitalization() != null) {
            score = score.add(stock.getTotalCapitalization().min(new BigDecimal("500")));
        }
        if (stock.getLastPrice() != null) {
            score = score.add(stock.getLastPrice().min(new BigDecimal("100")));
        }
        FinanceAnalysis finance = finance(stock.getId());
        if (finance != null && finance.getTotalIncome() != null) {
            score = score.add(finance.getTotalIncome().divide(new BigDecimal("100000000"), 2, RoundingMode.HALF_UP)
                    .min(new BigDecimal("300")));
        }
        return score.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return new BigDecimal(String.valueOf(value));
    }
}

