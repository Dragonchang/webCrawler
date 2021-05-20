package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.FinanceAnalysisRequestDTO;
import com.dragonchang.domain.dto.FinanceAnalysisResponseDTO;
import com.dragonchang.domain.dto.HolderCompanyListDTO;
import com.dragonchang.domain.dto.HolderDetailRequestDTO;
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
}
