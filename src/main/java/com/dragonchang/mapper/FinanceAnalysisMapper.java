package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dragonchang.domain.po.FinanceAnalysis;

import java.util.List;

public interface FinanceAnalysisMapper extends BaseMapper<FinanceAnalysis> {
    List<String> getReportTimeList();
}
