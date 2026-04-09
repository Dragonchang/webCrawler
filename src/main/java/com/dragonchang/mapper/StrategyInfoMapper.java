package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.StrategyRequestDTO;
import com.dragonchang.domain.po.StrategyInfo;
import org.apache.ibatis.annotations.Param;

public interface StrategyInfoMapper extends BaseMapper<StrategyInfo> {
    IPage<StrategyInfo> findPage(Page page, @Param("request") StrategyRequestDTO request);
}

