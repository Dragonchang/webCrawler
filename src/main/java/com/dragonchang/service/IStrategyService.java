package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.StrategyRequestDTO;
import com.dragonchang.domain.dto.StrategySaveRequestDTO;
import com.dragonchang.domain.po.StrategyInfo;
import com.dragonchang.domain.vo.JsonResult;

public interface IStrategyService {
    IPage<StrategyInfo> findPage(StrategyRequestDTO pageRequest);

    StrategyInfo getById(Long id);

    JsonResult saveStrategy(StrategySaveRequestDTO request);

    JsonResult publish(Long strategyId);

    JsonResult changeStatus(Long strategyId, Integer status);
}

