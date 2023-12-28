package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.FinanceAnalysisRequestDTO;
import com.dragonchang.domain.dto.FinanceAnalysisResponseDTO;
import com.dragonchang.domain.dto.HolderCompanyListDTO;
import com.dragonchang.domain.enums.FinanceReportTypeEnum;
import com.dragonchang.domain.po.ConceptStock;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.mapper.ConceptStockMapper;
import com.dragonchang.mapper.FinanceAnalysisMapper;
import com.dragonchang.service.ICompanyFinanceAnalysisService;
import com.dragonchang.util.DateUtil;
import com.dragonchang.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    ConceptStockMapper conceptStockMapper;

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

    @Override
    public ExcelData exportFlow(FinanceAnalysisRequestDTO request) {
        List<FinanceAnalysisResponseDTO> list = financeAnalysisMapper.findList(request);
        ExcelData data = new ExcelData();
        String time = DateUtil.localDateTimeFormat(LocalDateTime.now(),DateUtil.DEFAULT_DATE_TIME_FORMAT_SECOND);
        String fileName = "finance" + time;
        data.setSavePath("D:\\");
        data.setFileName(fileName);
        data.setSheetName("finance");
        List<String> titles = new ArrayList();
        titles.add("股票代码");
        titles.add("公司名称");
        titles.add("公司最新股价");
        titles.add("公司总市值(亿)");
        titles.add("总营收(亿)");
        titles.add("营收总市值百分比(%)");
        titles.add("营收同比增长(%)");
        titles.add("扣非利润(亿)");
        titles.add("扣非同比增长(%)");
        titles.add("扣非营收百分比(%)");
        titles.add("板块");
        titles.add("概念");
        titles.add("发布时间");
        titles.add("报告类型");
        titles.add("更新时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (int i = 0, length = list.size(); i < length; i++) {
            FinanceAnalysisResponseDTO finance = list.get(i);
            List<Object> row = new ArrayList();
            BigDecimal income = ExcelUtil.convertToBillion(finance.getTotalIncome());
            row.add(finance.getStockCode());
            row.add(finance.getName());
            row.add(finance.getLastPrice());
            row.add(finance.getTotalCapitalization());
            row.add(income);
            if(finance.getIncomeTotalPercent() != null) {
                row.add(finance.getIncomeTotalPercent().divide(BigDecimal.valueOf(100000000),2, RoundingMode.HALF_UP));
            }
            row.add(finance.getTotalAddPercent());
            row.add(ExcelUtil.convertToBillion(finance.getNetProfit()));
            row.add(finance.getNetProfitPercent());
            row.add(finance.getProfitTotalPercent());
            row.add(finance.getBkInfo());

            List<ConceptStock> conceptStockList = conceptStockMapper.selectList(new LambdaQueryWrapper<ConceptStock>()
                    .eq(ConceptStock::getCompanyStockId, finance.getStockCompanyId()));

            if(conceptStockList != null && !conceptStockList.isEmpty()) {
                String conceptInfo = "";
                for (ConceptStock stock: conceptStockList
                ) {
                    conceptInfo = conceptInfo + stock.getBkName() + " ";
                }
                row.add(conceptInfo);
            }

            row.add(finance.getReportTime());
            row.add(FinanceReportTypeEnum.getNameByCode(finance.getReportType()));
            row.add(finance.getUpdatedTime());

            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }
}
