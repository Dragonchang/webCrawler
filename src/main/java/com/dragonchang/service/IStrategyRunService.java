package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.StrategyResultDTO;
import com.dragonchang.domain.dto.StrategyRunDTO;
import com.dragonchang.domain.dto.StrategyRunPageRequestDTO;
import com.dragonchang.domain.dto.StrategyRunRequestDTO;
import com.dragonchang.domain.po.StrategyRunLog;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.domain.vo.StrategyRunPushMessageVO;

import java.util.List;

public interface IStrategyRunService {
    JsonResult execute(StrategyRunRequestDTO request);

    JsonResult createRun(StrategyRunRequestDTO request);

    void executeCreatedRun(Long runId);

    IPage<StrategyRunDTO> findPage(StrategyRunPageRequestDTO pageRequest);

    StrategyRunDTO getDetail(Long runId);

    List<StrategyRunLog> getLogs(Long runId);

    List<StrategyResultDTO> getResults(Long runId);

    StrategyRunPushMessageVO getRunSnapshot(Long runId);
}
