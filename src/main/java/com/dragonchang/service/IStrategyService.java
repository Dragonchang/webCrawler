package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.StrategyRequestDTO;
import com.dragonchang.domain.dto.StrategySaveRequestDTO;
import com.dragonchang.domain.dto.StrategyValidateRequestDTO;
import com.dragonchang.domain.po.StrategyInfo;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.domain.vo.ScriptTemplateVO;

import java.util.List;

public interface IStrategyService {
    IPage<StrategyInfo> findPage(StrategyRequestDTO pageRequest);

    StrategyInfo getById(Long id);

    JsonResult saveStrategy(StrategySaveRequestDTO request);

    JsonResult publish(Long strategyId);

    JsonResult changeStatus(Long strategyId, Integer status);

    JsonResult validateScript(StrategyValidateRequestDTO request);

    List<ScriptTemplateVO> getGroovyTemplates();
}

