package com.dragonchang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.StrategyResultDTO;
import com.dragonchang.domain.dto.StrategyRunDTO;
import com.dragonchang.domain.dto.StrategyRunPageRequestDTO;
import com.dragonchang.domain.dto.StrategyRunRequestDTO;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.domain.po.StrategyInfo;
import com.dragonchang.domain.po.StrategyResult;
import com.dragonchang.domain.po.StrategyRun;
import com.dragonchang.domain.po.StrategyRunLog;
import com.dragonchang.domain.po.StrategyVersion;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.domain.vo.StrategyRunPushMessageVO;
import com.dragonchang.mapper.StrategyInfoMapper;
import com.dragonchang.mapper.StrategyResultMapper;
import com.dragonchang.mapper.StrategyRunLogMapper;
import com.dragonchang.mapper.StrategyRunMapper;
import com.dragonchang.mapper.StrategyVersionMapper;
import com.dragonchang.service.IStrategyRunService;
import com.dragonchang.service.StrategyRunPushService;
import com.dragonchang.strategy.engine.StrategyExecuteContext;
import com.dragonchang.strategy.engine.StrategyExecuteResult;
import com.dragonchang.strategy.engine.StrategyExecutor;
import com.dragonchang.strategy.engine.StrategyExecutorFactory;
import com.dragonchang.strategy.support.StrategyFinanceDataService;
import com.dragonchang.strategy.support.StrategyUniverseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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
    private StrategyUniverseService strategyUniverseService;
    @Autowired
    private StrategyFinanceDataService strategyFinanceDataService;
    @Autowired
    private StrategyExecutorFactory strategyExecutorFactory;
    @Autowired
    private StrategyRunPushService strategyRunPushService;

    @Override
    public JsonResult execute(StrategyRunRequestDTO request) {
        JsonResult createResult = createRun(request);
        if (!createResult.isSuccess()) {
            return createResult;
        }
        Map<String, Object> result = (Map<String, Object>) createResult.getData();
        executeCreatedRun(Long.valueOf(String.valueOf(result.get("runId"))));
        return createResult;
    }

    @Override
    public JsonResult createRun(StrategyRunRequestDTO request) {
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

        String scriptType = StringUtils.defaultIfBlank(version.getScriptType(), info.getScriptType());
        StrategyRun run = new StrategyRun();
        run.setStrategyId(info.getId());
        run.setStrategyVersionId(version.getId());
        run.setEngineType(scriptType);
        run.setScriptType(scriptType);
        run.setRunType("MANUAL");
        run.setTriggerSource("USER");
        run.setRunStatus("RUNNING");
        run.setParamSnapshot(StringUtils.defaultIfBlank(request.getParams(), info.getDefaultParams()));
        run.setDataSnapshotDate(StringUtils.defaultIfBlank(request.getDataSnapshotDate(), LocalDateTime.now().toLocalDate().toString()));
        run.setUniverseSnapshot(info.getUniverseConfig());
        run.setStartTime(LocalDateTime.now());
        run.setCreatedTime(LocalDateTime.now());
        strategyRunMapper.insert(run);

        strategyRunPushService.pushProgress(run.getId(), info.getId(), "RUNNING", 3, "RUN_CREATED", "运行任务已创建，等待后台执行");
        strategyRunPushService.pushLog(run.getId(), info.getId(), "INFO", LocalDateTime.now().toString(), "运行记录创建成功，runId=" + run.getId());
        log.info("strategy run created, runId={}, strategyId={}, scriptType={}, params={}", run.getId(), info.getId(), scriptType, run.getParamSnapshot());

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("runId", run.getId());
        result.put("status", run.getRunStatus());
        result.put("resultCount", 0);
        return JsonResult.success(result);
    }

    @Override
    public void executeCreatedRun(Long runId) {
        StrategyRun run = strategyRunMapper.selectById(runId);
        if (run == null) {
            log.error("executeCreatedRun failed, run not found, runId={}", runId);
            return;
        }
        StrategyInfo info = strategyInfoMapper.selectById(run.getStrategyId());
        StrategyVersion version = strategyVersionMapper.selectById(run.getStrategyVersionId());
        if (info == null || version == null) {
            log.error("executeCreatedRun failed, info or version missing, runId={}", runId);
            return;
        }

        try {
            strategyRunPushService.pushProgress(run.getId(), info.getId(), "RUNNING", 8, "RUN_BOOTSTRAP", "开始准备策略运行上下文");
            strategyRunPushService.pushLog(run.getId(), info.getId(), "INFO", LocalDateTime.now().toString(), "开始执行策略，strategyId=" + info.getId() + ", versionId=" + version.getId());

            JSONObject params = parseParams(run.getParamSnapshot());
            strategyRunPushService.pushProgress(run.getId(), info.getId(), "RUNNING", 15, "PARAMS_READY", "参数解析完成，开始加载股票池");
            strategyRunPushService.pushLog(run.getId(), info.getId(), "INFO", LocalDateTime.now().toString(), "参数解析完成：" + params.toJSONString());
            log.info("strategy run params parsed, runId={}, params={}", run.getId(), params.toJSONString());

            List<CompanyStock> universe = strategyUniverseService.loadUniverse(info.getUniverseConfig());
            strategyRunPushService.pushProgress(run.getId(), info.getId(), "RUNNING", 28, "UNIVERSE_READY", "股票池加载完成，共" + universe.size() + "只");
            strategyRunPushService.pushLog(run.getId(), info.getId(), "INFO", LocalDateTime.now().toString(), "股票池加载完成，universeSize=" + universe.size());
            log.info("strategy run universe loaded, runId={}, universeSize={}", run.getId(), universe.size());

            Map<Integer, FinanceAnalysis> financeMap = strategyFinanceDataService.latestFinanceMap(
                    universe.stream().map(CompanyStock::getId).collect(Collectors.toList()));
            strategyRunPushService.pushProgress(run.getId(), info.getId(), "RUNNING", 42, "FINANCE_READY", "财务数据预加载完成，共" + financeMap.size() + "条");
            strategyRunPushService.pushLog(run.getId(), info.getId(), "INFO", LocalDateTime.now().toString(), "财务数据预加载完成，financeSize=" + financeMap.size());
            log.info("strategy run finance map loaded, runId={}, financeSize={}", run.getId(), financeMap.size());

            StrategyExecuteContext context = new StrategyExecuteContext();
            context.setRunId(run.getId());
            context.setStrategyId(info.getId());
            context.setStrategyInfo(info);
            context.setStrategyVersion(version);
            context.setParams(params);
            context.setDataSnapshotDate(run.getDataSnapshotDate());
            context.setUniverse(universe);
            context.setFinanceMap(financeMap);
            context.setPushService(strategyRunPushService);

            String scriptType = StringUtils.defaultIfBlank(version.getScriptType(), info.getScriptType());
            StrategyExecutor executor = strategyExecutorFactory.getExecutor(scriptType);
            strategyRunPushService.pushProgress(run.getId(), info.getId(), "RUNNING", 55, "ENGINE_SELECTED", "开始执行策略引擎：" + scriptType);
            strategyRunPushService.pushLog(run.getId(), info.getId(), "INFO", LocalDateTime.now().toString(), "已选择执行器：" + scriptType);
            log.info("strategy executor selected, runId={}, executorType={}", run.getId(), scriptType);

            StrategyExecuteResult executeResult = executor.execute(context);
            strategyRunPushService.pushProgress(run.getId(), info.getId(), "RUNNING", 96, "PERSISTING", "策略执行完成，开始保存结果和日志");
            strategyRunPushService.pushLog(run.getId(), info.getId(), "INFO", LocalDateTime.now().toString(), "策略执行完成，准备持久化，resultCount=" + executeResult.getResultCount() + ", logCount=" + executeResult.getLogs().size());
            log.info("strategy executor finished, runId={}, resultCount={}, logCount={}", run.getId(), executeResult.getResultCount(), executeResult.getLogs().size());

            saveResults(run.getId(), info.getId(), executeResult.getResults());
            saveLogs(run.getId(), info.getId(), executeResult.getLogs());

            run.setResultCount(executeResult.getResultCount());
            run.setRunStatus("SUCCESS");
            run.setEndTime(LocalDateTime.now());
            run.setDurationMs(Duration.between(run.getStartTime(), run.getEndTime()).toMillis());
            strategyRunMapper.updateById(run);

            info.setLastRunTime(run.getEndTime());
            info.setLastRunStatus(run.getRunStatus());
            info.setUpdatedTime(LocalDateTime.now());
            strategyInfoMapper.updateById(info);

            strategyRunPushService.pushCompleted(run.getId(), info.getId(), "SUCCESS", 100, run.getResultCount(), "策略运行完成");
            strategyRunPushService.pushLog(run.getId(), info.getId(), "INFO", LocalDateTime.now().toString(), "策略运行成功，耗时=" + run.getDurationMs() + "ms，结果数量=" + run.getResultCount());
            log.info("strategy run success, runId={}, durationMs={}, resultCount={}", run.getId(), run.getDurationMs(), run.getResultCount());
        } catch (Exception e) {
            String errorMessage = e.getMessage() == null ? e.toString() : e.getMessage();
            log.error("execute strategy error, runId={}, strategyId={}", run.getId(), run.getStrategyId(), e);
            StrategyRunLog errorLog = new StrategyRunLog();
            errorLog.setRunId(run.getId());
            errorLog.setLineNo(9999);
            errorLog.setLogLevel("ERROR");
            errorLog.setContent(errorMessage);
            errorLog.setLogTime(LocalDateTime.now());
            strategyRunLogMapper.insert(errorLog);
            strategyRunPushService.pushLog(run.getId(), info.getId(), "ERROR", LocalDateTime.now().toString(), errorMessage);
            strategyRunPushService.pushProgress(run.getId(), info.getId(), "FAIL", 100, "FAILED", "策略运行失败：" + errorMessage);
            run.setRunStatus("FAIL");
            run.setErrorMessage(errorMessage);
            run.setEndTime(LocalDateTime.now());
            if (run.getStartTime() != null && run.getEndTime() != null) {
                run.setDurationMs(Duration.between(run.getStartTime(), run.getEndTime()).toMillis());
            }
            strategyRunMapper.updateById(run);
            info.setLastRunTime(run.getEndTime());
            info.setLastRunStatus(run.getRunStatus());
            info.setUpdatedTime(LocalDateTime.now());
            strategyInfoMapper.updateById(info);
            strategyRunPushService.pushCompleted(run.getId(), info.getId(), "FAIL", 100, 0, "策略运行失败：" + errorMessage);
        }
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

    private void saveLogs(Long runId, Long strategyId, List<StrategyRunLog> logs) {
        int total = logs == null ? 0 : logs.size();
        int current = 0;
        for (StrategyRunLog logItem : logs) {
            strategyRunLogMapper.insert(logItem);
            current++;
            if (strategyRunPushService != null && (current == total || current % 20 == 0)) {
                strategyRunPushService.pushStepProgress(runId, strategyId, "RUNNING", 97,
                        "LOG_SAVING", current, total, "正在保存运行日志，已完成" + current + "/" + total);
            }
        }
    }

    private void saveResults(Long runId, Long strategyId, List<StrategyResult> results) {
        int total = results == null ? 0 : results.size();
        int current = 0;
        for (StrategyResult result : results) {
            strategyResultMapper.insert(result);
            current++;
            if (strategyRunPushService != null && (current == total || current % 20 == 0)) {
                strategyRunPushService.pushStepProgress(runId, strategyId, "RUNNING", 96,
                        "RESULT_SAVING", current, total, "正在保存策略结果，已完成" + current + "/" + total);
            }
        }
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
        return strategyResultMapper.selectList(new LambdaQueryWrapper<StrategyResult>()
                .eq(StrategyResult::getRunId, runId)
                .orderByAsc(StrategyResult::getRankNo))
                .stream()
                .map(item -> {
                    StrategyResultDTO dto = new StrategyResultDTO();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public StrategyRunPushMessageVO getRunSnapshot(Long runId) {
        return strategyRunPushService.getSnapshot(runId);
    }
}
