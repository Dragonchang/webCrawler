package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.*;
import com.dragonchang.domain.po.FinanceAnalysis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FinanceAnalysisMapper extends BaseMapper<FinanceAnalysis> {
    List<String> getReportTimeList();

    /**
     *
     * @param page
     * @param request
     * @return
     */
    IPage<FinanceAnalysisResponseDTO> findPage(Page page, @Param("request") FinanceAnalysisRequestDTO request);

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<FinanceAnalysisResponseDTO> findList(@Param("request") FinanceAnalysisRequestDTO request);

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<FinanceAnalysisResponseDTO> findRecommendList(@Param("request") RecommendAnalysisDTO request);
}
