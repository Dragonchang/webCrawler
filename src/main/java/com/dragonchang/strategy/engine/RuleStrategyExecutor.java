package com.dragonchang.strategy.engine;

import com.alibaba.fastjson.JSONObject;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.domain.po.StrategyResult;
import com.dragonchang.strategy.api.LogApi;
import com.dragonchang.strategy.support.StrategyFinanceDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class RuleStrategyExecutor implements StrategyExecutor {

    @Autowired
    private StrategyFinanceDataService strategyFinanceDataService;

    @Override
    public String getScriptType() {
        return "RULE";
    }

    @Override
    public StrategyExecuteResult execute(StrategyExecuteContext context) {
        StrategyExecuteResult executeResult = new StrategyExecuteResult();
        LogApi logApi = new LogApi(context.getRunId(), context.getStrategyId(), context.getPushService());
        JSONObject params = context.getParams();
        logApi.info("开始执行规则策略：" + context.getStrategyInfo().getStrategyName());
        logApi.info("本次运行参数：" + params.toJSONString());
        logApi.info("加载股票池完成，数量=" + context.getUniverse().size());
        if (context.getPushService() != null) {
            context.getPushService().pushProgress(context.getRunId(), context.getStrategyId(), "RUNNING", 65, "RULE_FILTER", "规则引擎开始筛选股票");
        }

        BigDecimal minMarketCap = params.getBigDecimal("minMarketCap");
        BigDecimal maxPe = params.getBigDecimal("maxPe");
        BigDecimal minPrice = params.getBigDecimal("minPrice");
        BigDecimal minIncome = params.getBigDecimal("minIncome");
        Integer limit = params.getInteger("limit");
        if (limit == null || limit <= 0) {
            limit = 20;
        }

        List<CompanyStock> filtered = new ArrayList<CompanyStock>();
        int total = context.getUniverse() == null ? 0 : context.getUniverse().size();
        int processed = 0;
        for (CompanyStock stock : context.getUniverse()) {
            processed++;
            if (stock.getLastPrice() == null || stock.getTotalCapitalization() == null) {
                continue;
            }
            if (minMarketCap != null && stock.getTotalCapitalization().compareTo(minMarketCap) < 0) {
                continue;
            }
            if (minPrice != null && stock.getLastPrice().compareTo(minPrice) < 0) {
                continue;
            }
            if (maxPe != null && StringUtils.isNotBlank(stock.getSyl())) {
                try {
                    BigDecimal pe = new BigDecimal(stock.getSyl());
                    if (pe.compareTo(maxPe) > 0) {
                        continue;
                    }
                } catch (Exception e) {
                    logApi.warn("市盈率转换失败，stockId=" + stock.getId() + ", syl=" + stock.getSyl());
                    continue;
                }
            } else if (maxPe != null) {
                continue;
            }
            FinanceAnalysis finance = context.getFinanceMap() == null ? strategyFinanceDataService.latestFinance(stock.getId()) : context.getFinanceMap().get(stock.getId());
            if (minIncome != null) {
                if (finance == null || finance.getTotalIncome() == null || finance.getTotalIncome().compareTo(minIncome) < 0) {
                    continue;
                }
            }
            filtered.add(stock);
            if (context.getPushService() != null && (processed == total || processed % 50 == 0)) {
                int progress = 65 + Math.min(20, total == 0 ? 0 : (processed * 20 / total));
                context.getPushService().pushStepProgress(context.getRunId(), context.getStrategyId(), "RUNNING", progress,
                        "RULE_FILTER", processed, total, "规则筛选中，已处理" + processed + "/" + total + "，命中" + filtered.size() + "只");
            }
        }

        filtered.sort(Comparator.comparing(CompanyStock::getTotalCapitalization, Comparator.nullsLast(Comparator.reverseOrder())));
        if (filtered.size() > limit) {
            filtered = filtered.subList(0, limit);
        }
        logApi.info("规则筛选完成，命中数量=" + filtered.size() + "，截断后数量=" + filtered.size());

        if (context.getPushService() != null) {
            context.getPushService().pushProgress(context.getRunId(), context.getStrategyId(), "RUNNING", 88, "RULE_RESULT_BUILD", "开始构建策略结果");
        }

        int rank = 1;
        int resultTotal = filtered.size();
        for (CompanyStock stock : filtered) {
            FinanceAnalysis finance = context.getFinanceMap() == null ? strategyFinanceDataService.latestFinance(stock.getId()) : context.getFinanceMap().get(stock.getId());
            StrategyResult result = new StrategyResult();
            result.setRunId(context.getRunId());
            result.setStockId(stock.getId());
            result.setStockCode(stock.getStockCode());
            result.setStockName(stock.getName());
            result.setActionType("WATCH");
            result.setScore(buildScore(stock, finance));
            result.setReason(buildReason(stock, finance));
            result.setFactorDetail(buildFactorDetail(stock, finance));
            result.setRankNo(rank);
            result.setCreatedTime(LocalDateTime.now());
            executeResult.getResults().add(result);
            if (context.getPushService() != null && (rank == resultTotal || rank % 10 == 0)) {
                int progress = 88 + Math.min(7, resultTotal == 0 ? 0 : (rank * 7 / resultTotal));
                context.getPushService().pushStepProgress(context.getRunId(), context.getStrategyId(), "RUNNING", progress,
                        "RULE_RESULT_BUILD", rank, resultTotal, "正在构建结果集，已生成" + rank + "/" + resultTotal);
            }
            rank++;
        }
        executeResult.setLogs(logApi.getLogs());
        executeResult.setResultCount(executeResult.getResults().size());
        executeResult.setProgress(95);
        executeResult.setStatus("SUCCESS");
        logApi.info("规则执行完成，结果数量=" + executeResult.getResults().size());
        return executeResult;
    }

    private BigDecimal buildScore(CompanyStock stock, FinanceAnalysis finance) {
        BigDecimal score = BigDecimal.ZERO;
        if (stock.getTotalCapitalization() != null) {
            score = score.add(stock.getTotalCapitalization().min(new BigDecimal("500")));
        }
        if (stock.getLastPrice() != null) {
            score = score.add(stock.getLastPrice().min(new BigDecimal("100")));
        }
        if (finance != null && finance.getTotalIncome() != null) {
            score = score.add(finance.getTotalIncome().divide(new BigDecimal("100000000"), 2, RoundingMode.HALF_UP)
                    .min(new BigDecimal("300")));
        }
        return score.setScale(2, RoundingMode.HALF_UP);
    }

    private String buildReason(CompanyStock stock, FinanceAnalysis finance) {
        List<String> reasons = new ArrayList<String>();
        if (stock.getTotalCapitalization() != null) {
            reasons.add("总市值=" + stock.getTotalCapitalization() + "亿");
        }
        if (StringUtils.isNotBlank(stock.getSyl())) {
            reasons.add("市盈率=" + stock.getSyl());
        }
        if (finance != null && finance.getTotalIncome() != null) {
            reasons.add("最近营收=" + finance.getTotalIncome());
        }
        return StringUtils.join(reasons, "；");
    }

    private String buildFactorDetail(CompanyStock stock, FinanceAnalysis finance) {
        JSONObject detail = new JSONObject();
        detail.put("lastPrice", stock.getLastPrice());
        detail.put("totalCapitalization", stock.getTotalCapitalization());
        detail.put("syl", stock.getSyl());
        detail.put("totalIncome", finance == null ? null : finance.getTotalIncome());
        detail.put("netProfit", finance == null ? null : finance.getNetProfit());
        return detail.toJSONString();
    }
}
