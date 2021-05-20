package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.FinanceAnalysisRequestDTO;
import com.dragonchang.domain.dto.FinanceAnalysisResponseDTO;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.mapper.FinanceAnalysisMapper;
import com.dragonchang.service.ICompanyFinanceAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-19 17:55
 **/
@Slf4j
@Service
public class CompanyFinanceAnalysisService implements ICompanyFinanceAnalysisService {

    @Autowired
    private FinanceAnalysisMapper financeAnalysisMapper;

    @Override
    public List<String> getTotalReportTimeList() {
        return financeAnalysisMapper.getReportTimeList();
    }

    @Override
    public IPage<FinanceAnalysisResponseDTO> findPage(FinanceAnalysisRequestDTO pageRequest) {
        Page page = new Page();
        page.setCurrent(pageRequest.getPage());
        page.setSize(pageRequest.getSize());
        return financeAnalysisMapper.findPage(page, pageRequest);
    }
}
