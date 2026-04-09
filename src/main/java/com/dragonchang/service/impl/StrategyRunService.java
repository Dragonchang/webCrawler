package com.dragonchang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.StrategyResultDTO;
import com.dragonchang.domain.dto.StrategyRunDTO;
import com.dragonchang.domain.dto.StrategyRunPageRequestDTO;
import com.dragonchang.domain.dto.StrategyRunRequestDTO;
import com.dragonchang.domain.po.BkStock;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.domain.po.StrategyInfo;
import com.dragonchang.domain.po.StrategyResult;
import com.dragonchang.domain.po.StrategyRun;
import com.dragonchang.domain.po.StrategyRunLog;
import com.dragonchang.domain.po.StrategyVersion;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.mapper.BkStockMapper;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.mapper.FinanceAnalysisMapper;
import com.dragonchang.mapper.StrategyInfoMapper;
import com.dragonchang.mapper.StrategyResultMapper;
import com.dragonchang.mapper.StrategyRunLogMapper;
import com.dragonchang.mapper.StrategyRunMapper;
import com.dragonchang.mapper.StrategyVersionMapper;
import com.dragonchang.service.IStrategyRunService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StrategyRunService implements IStrategyRunService {

    @Autowired
    private StrategyRunMapper strategyRunMapper;
    @Autowired
    private StrategyRunLogMapper strategyRunLogMapper;
    @Autowired
    private StrategyResultMapper strategyResultMapper;
    @Autowired
    private StrategyInfoMapper strategyInfoMapper;
    @Autowired
    private StrategyVersionMapper strategyVersionMapper;
    @Autowired
    private CompanyStockMapper companyStockMapper;
    @Autowired
    private FinanceAnalysisMapper financeAnalysisMapper;
    @Autowired
    private BkStockMapper bkStockMapper;

    @Override
    public JsonResult execute(StrategyRunRequestDTO request) {
        if (request.getStrategyId() == null) {
            return JsonResult.failure("策略ID不能为空");
        }
        StrategyInfo info = strategyInfoMapper.selectById(request.getStrategyId());
        if (info == null) {
            return JsonResult.failure("策略不存在");
        }
        if (info.getPublishedVersionNo() == null) {
            return JsonResult.failure("请先发布策略后再运行");
        }
        StrategyVersion version = strategyVersionMapper.selectOne(new LambdaQueryWrapper<StrategyVersion>()
                .eq(StrategyVersion::getStrategyId, info.getId())
                .eq(StrategyVersion::getVersionNo, info.getPublishedVersionNo()));
        if (version == null) {
            return JsonResult.failure("策略发布版本不存在");
        }

        StrategyRun run = new StrategyRun();
        run.setStrategyId(info.getId());
        run.setStrategyVersionId(version.getId());
        run.setRunType("MANUAL");
        run.setTriggerSource("USER");
        run.setRunStatus("RUNNING");
        run.setParamSnapshot(StringUtils.defaultIfBlank(request.getParams(), info.getDefaultParams()));
        run.setDataSnapshotDate(StringUtils.defaultIfBlank(request.getDataSnapshotDate(), LocalDateTime.now().toLocalDate().toString()));
        run.setUniverseSnapshot(info.getUniverseConfig());
        run.setStartTime(LocalDateTime.now());
        run.setCreatedTime(LocalDateTime.now());
        strategyRunMapper.insert(run);

        List<StrategyRunLog> logs = new ArrayList<>();
        try {
            addLog(logs, run.getId(), 1, "INFO", "开始执行策略：" + info.getStrategyName());
            JSONObject params = parseParams(run.getParamSnapshot());
            addLog(logs, run.getId(), 2, "INFO", "本次运行参数：" + params.toJSONString());
            List<CompanyStock> universe = loadUniverse(info.getUniverseConfig());
            addLog(logs, run.getId(), 3, "INFO", "加载股票池完成，数量=" + universe.size());
            List<StrategyResult> results = executeRule(universe, params, run.getId());
            for (StrategyResult result : results) {
                strategyResultMapper.insert(result);
            }
            run.setResultCount(results.size());
            run.setRunStatus("SUCCESS");
            run.setEndTime(LocalDateTime.now());
            run.setDurationMs(java.time.Duration.between(run.getStartTime(), run.getEndTime()).toMillis());
            addLog(logs, run.getId(), 4, "INFO", "执行完成，结果数量=" + results.size());
            strategyRunMapper.updateById(run);

            info.setLastRunTime(run.getEndTime());
            info.setLastRunStatus(run.getRunStatus());
            info.setUpdatedTime(LocalDateTime.now());
            strategyInfoMapper.updateById(info);
        } catch (Exception e) {
            log.error("execute strategy error", e);
            addLog(logs, run.getId(), logs.size() + 1, "ERROR", e.getMessage());
            run.setRunStatus("FAIL");
            run.setErrorMessage(e.getMessage());
            run.setEndTime(LocalDateTime.now());
            if (run.getStartTime() != null && run.getEndTime() != null) {
                run.setDurationMs(java.time.Duration.between(run.getStartTime(), run.getEndTime()).toMillis());
            }
            strategyRunMapper.updateById(run);
            info.setLastRunTime(run.getEndTime());
            info.setLastRunStatus(run.getRunStatus());
            info.setUpdatedTime(LocalDateTime.now());
            strategyInfoMapper.updateById(info);
            saveLogs(logs);
            return JsonResult.failure("执行失败：" + e.getMessage());
        }
        saveLogs(logs);
        Map<String, Object> result = new HashMap<>();
        result.put("runId", run.getId());
        result.put("status", run.getRunStatus());
        result.put("resultCount", run.getResultCount());
        return JsonResult.success(result);
    }

    private void saveLogs(List<StrategyRunLog> logs) {
        for (StrategyRunLog logItem : logs) {
            strategyRunLogMapper.insert(logItem);
        }
    }

    private void addLog(List<StrategyRunLog> logs, Long runId, int lineNo, String level, String content) {
        StrategyRunLog logItem = new StrategyRunLog();
        logItem.setRunId(runId);
        logItem.setLineNo(lineNo);
        logItem.setLogLevel(level);
        logItem.setContent(content);
        logItem.setLogTime(LocalDateTime.now());
        logs.add(logItem);
    }

    private JSONObject parseParams(String json) {
        if (StringUtils.isBlank(json)) {
            return new JSONObject();
        }
        try {
            return JSON.parseObject(json);
        } catch (Exception e) {
            JSONObject ret = new JSONObject();
            ret.put("raw", json);
            return ret;
        }
    }

    private List<CompanyStock> loadUniverse(String universeConfig) {
        LambdaQueryWrapper<CompanyStock> wrapper = new LambdaQueryWrapper<CompanyStock>().eq(CompanyStock::getDeleted, 0);
        if (StringUtils.isBlank(universeConfig)) {
            return companyStockMapper.selectList(wrapper);
        }
        JSONObject config;
        try {
            config = JSON.parseObject(universeConfig);
        } catch (Exception e) {
            return companyStockMapper.selectList(wrapper);
        }
        String type = config.getString("type");
        if (StringUtils.equalsIgnoreCase(type, "BK")) {
            String bkName = config.getString("bkName");
            List<BkStock> bkStocks = bkStockMapper.selectList(new LambdaQueryWrapper<BkStock>()
                    .eq(BkStock::getDeleted, 0)
                    .eq(BkStock::getBkName, bkName));
            List<Integer> ids = bkStocks.stream().map(BkStock::getCompanyStockId).collect(Collectors.toList());
            if (ids.isEmpty()) {
                return new ArrayList<>();
            }
            wrapper.in(CompanyStock::getId, ids);
        } else if (StringUtils.equalsIgnoreCase(type, "IDS")) {
            JSONArray arr = config.getJSONArray("stockIds");
            if (arr != null && !arr.isEmpty()) {
                wrapper.in(CompanyStock::getId, arr.toJavaList(Integer.class));
            }
        }
        return companyStockMapper.selectList(wrapper);
    }

    private List<StrategyResult> executeRule(List<CompanyStock> universe, JSONObject params, Long runId) {
        BigDecimal minMarketCap = params.getBigDecimal("minMarketCap");
        BigDecimal maxPe = params.getBigDecimal("maxPe");
        BigDecimal minPrice = params.getBigDecimal("minPrice");
        BigDecimal minIncome = params.getBigDecimal("minIncome");
        Integer limit = params.getInteger("limit");
        if (limit == null || limit <= 0) {
            limit = 20;
        }

        List<CompanyStock> filtered = new ArrayList<>();
        for (CompanyStock stock : universe) {
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
                    continue;
                }
            } else if (maxPe != null) {
                continue;
            }
            FinanceAnalysis finance = financeAnalysisMapper.selectOne(new LambdaQueryWrapper<FinanceAnalysis>()
                    .eq(FinanceAnalysis::getStockCompanyId, stock.getId())
                    .orderByDesc(FinanceAnalysis::getReportTime)
                    .last("limit 1"));
            if (minIncome != null) {
                if (finance == null || finance.getTotalIncome() == null || finance.getTotalIncome().compareTo(minIncome) < 0) {
                    continue;
                }
            }
            filtered.add(stock);
        }

        filtered.sort(Comparator.comparing(CompanyStock::getTotalCapitalization, Comparator.nullsLast(Comparator.reverseOrder())));
        if (filtered.size() > limit) {
            filtered = filtered.subList(0, limit);
        }

        List<StrategyResult> results = new ArrayList<>();
        int rank = 1;
        for (CompanyStock stock : filtered) {
            FinanceAnalysis finance = financeAnalysisMapper.selectOne(new LambdaQueryWrapper<FinanceAnalysis>()
                    .eq(FinanceAnalysis::getStockCompanyId, stock.getId())
                    .orderByDesc(FinanceAnalysis::getReportTime)
                    .last("limit 1"));
            BigDecimal score = buildScore(stock, finance);
            JSONObject detail = new JSONObject();
            detail.put("lastPrice", stock.getLastPrice());
            detail.put("totalCapitalization", stock.getTotalCapitalization());
            detail.put("syl", stock.getSyl());
            detail.put("totalIncome", finance == null ? null : finance.getTotalIncome());
            detail.put("netProfit", finance == null ? null : finance.getNetProfit());

            StrategyResult result = new StrategyResult();
            result.setRunId(runId);
            result.setStockId(stock.getId());
            result.setStockCode(stock.getStockCode());
            result.setStockName(stock.getName());
            result.setActionType("WATCH");
            result.setScore(score);
            result.setReason(buildReason(stock, finance));
            result.setFactorDetail(detail.toJSONString());
            result.setRankNo(rank++);
            result.setCreatedTime(LocalDateTime.now());
            results.add(result);
        }
        return results;
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
            score = score.add(finance.getTotalIncome().divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP)
                    .min(new BigDecimal("300")));
        }
        return score.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private String buildReason(CompanyStock stock, FinanceAnalysis finance) {
        List<String> reasons = new ArrayList<>();
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

    @Override
    public IPage<StrategyRunDTO> findPage(StrategyRunPageRequestDTO pageRequest) {
        Page page = new Page();
        page.setCurrent(pageRequest.getPage());
        page.setSize(pageRequest.getSize());
        return strategyRunMapper.findPage(page, pageRequest);
    }

    @Override
    public StrategyRunDTO getDetail(Long runId) {
        return strategyRunMapper.getDetail(runId);
    }

    @Override
    public List<StrategyRunLog> getLogs(Long runId) {
        return strategyRunLogMapper.selectList(new LambdaQueryWrapper<StrategyRunLog>()
                .eq(StrategyRunLog::getRunId, runId)
                .orderByAsc(StrategyRunLog::getLineNo));
    }

    @Override
    public List<StrategyResultDTO> getResults(Long runId) {
        List<StrategyResult> list = strategyResultMapper.selectList(new LambdaQueryWrapper<StrategyResult>()
                .eq(StrategyResult::getRunId, runId)
                .orderByAsc(StrategyResult::getRankNo));
        List<StrategyResultDTO> ret = new ArrayList<>();
        for (StrategyResult result : list) {
            StrategyResultDTO dto = new StrategyResultDTO();
            BeanUtils.copyProperties(result, dto);
            dto.setScore(result.getScore() == null ? null : result.getScore().toPlainString());
            ret.add(dto);
        }
        return ret;
    }
}

