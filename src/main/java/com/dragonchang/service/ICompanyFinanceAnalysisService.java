package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.FinanceAnalysisRequestDTO;
import com.dragonchang.domain.dto.FinanceAnalysisResponseDTO;
import com.dragonchang.domain.dto.HolderDetailRequestDTO;
import com.dragonchang.domain.po.FinanceAnalysis;

import java.util.List;

public interface ICompanyFinanceAnalysisService {

    /**
     * 获取年度发布时间列表
     *
     * @return
     */
    List<String> getTotalReportTimeList();

    /**
     * 分页查询公司信息
     *
     * @param pageRequest
     * @return
     */
    IPage<FinanceAnalysisResponseDTO> findPage(FinanceAnalysisRequestDTO pageRequest);

    /**
     * 导出持有穿透
     * @param request
     * @return
     */
    ExcelData exportFlow(FinanceAnalysisRequestDTO request);
}
