package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.StrategyRunDTO;
import com.dragonchang.domain.dto.StrategyRunPageRequestDTO;
import com.dragonchang.domain.po.StrategyRun;
import org.apache.ibatis.annotations.Param;

public interface StrategyRunMapper extends BaseMapper<StrategyRun> {
    IPage<StrategyRunDTO> findPage(Page page, @Param("request") StrategyRunPageRequestDTO request);

    StrategyRunDTO getDetail(@Param("runId") Long runId);
}

