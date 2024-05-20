package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dragonchang.crawler.EastMoneyCrawler;
import com.dragonchang.domain.dto.eastmoney.FinanceAnalysisDataDTO;
import com.dragonchang.domain.dto.eastmoney.FinanceReportTimeDTO;
import com.dragonchang.domain.dto.eastmoney.KlineDetailDTO;
import com.dragonchang.domain.dto.eastmoney.TodayPriceDTO;
import com.dragonchang.domain.enums.FinanceReportTypeEnum;
import com.dragonchang.domain.po.CompanyPriceRecord;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.mapper.CompanyPriceRecordMapper;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.mapper.FinanceAnalysisMapper;
import com.dragonchang.service.ICompanyPriceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-17 19:21
 **/
@Slf4j
@Service
public class CompanyPriceRecordService implements ICompanyPriceRecordService {
    @Autowired
    private CompanyStockMapper companyStockMapper;
    @Autowired
    private CompanyPriceRecordMapper companyPriceRecordMapper;
    @Autowired
    private FinanceAnalysisMapper financeAnalysisMapper;
    @Autowired
    private EastMoneyCrawler eastMoneyCrawler;

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void syncCompanyPrice(Integer companyStockId) {
        List<CompanyStock> companyStockList = companyStockMapper.selectList(new LambdaQueryWrapper<CompanyStock>());
        for (CompanyStock stock : companyStockList) {
            if (companyStockId != null && stock.getId() < companyStockId) {
                continue;
            }
            KlineDetailDTO dto = eastMoneyCrawler.getKlineData(stock.getStockCode());
            if (dto != null) {
                for (String kline : dto.getKlines()) {
                    if (StringUtils.isNotBlank(kline)) {
                        String[] line = kline.split(",");
                        if (line != null && line.length > 1) {
                            CompanyPriceRecord record = new CompanyPriceRecord();
                            record.setCompanyStockId(stock.getId());
                            record.setOpenPrice(line[1]);
                            record.setClosePrice(line[2]);
                            record.setHighestPrice(line[3]);
                            record.setLowestPrice(line[4]);
                            record.setReportTime(line[0]);
                            CompanyPriceRecord query = companyPriceRecordMapper.selectOne(new LambdaQueryWrapper<CompanyPriceRecord>()
                                    .eq(CompanyPriceRecord::getCompanyStockId, stock.getId())
                                    .eq(CompanyPriceRecord::getReportTime, line[0]));
                            if (query != null) {
                                continue;
                            }
                            companyPriceRecordMapper.insert(record);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void syncCompanyTodayPrice() {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = sdf.format(d);
        System.out.println("格式化后的日期：" + currentTime);
        List<CompanyStock> companyStockList = companyStockMapper.selectList(new LambdaQueryWrapper<CompanyStock>());
        for (CompanyStock stock : companyStockList) {
            TodayPriceDTO priceDTO = eastMoneyCrawler.getTodayPrice(stock.getStockCode());
            if(priceDTO == null) {
                continue;
            }
            CompanyPriceRecord record = new CompanyPriceRecord();
            record.setOpenPrice(priceDTO.getF46());
            record.setClosePrice(priceDTO.getF43());
            record.setHighestPrice(priceDTO.getF44());
            record.setLowestPrice(priceDTO.getF45());
            record.setCompanyStockId(stock.getId());
            record.setReportTime(currentTime);

            record.setDtzf(stock.getDtzf());
            record.setDtcjl(stock.getDtcjl());
            record.setDtcjje(stock.getDtcjje());
            record.setDthsl(stock.getDthsl());
            record.setLb(stock.getLb());
            record.setSyl(stock.getSyl());

            CompanyPriceRecord query = companyPriceRecordMapper.selectOne(new LambdaQueryWrapper<CompanyPriceRecord>()
                    .eq(CompanyPriceRecord::getCompanyStockId, stock.getId())
                    .eq(CompanyPriceRecord::getReportTime, currentTime));
            if (query != null) {
                continue;
            }
            companyPriceRecordMapper.insert(record);
        }
    }

    @Override
    public List<List<String>> getPriceRecordByCompany(Integer companyStockId) {
        List<List<String>> ret = new ArrayList<>();
        List<CompanyPriceRecord> priceRecords = companyPriceRecordMapper.selectList(new LambdaQueryWrapper<CompanyPriceRecord>()
                .eq(CompanyPriceRecord::getCompanyStockId, companyStockId));
        for (CompanyPriceRecord record : priceRecords) {
            List<String> strRecords = new ArrayList<>();
            strRecords.add(record.getReportTime());
            strRecords.add(record.getOpenPrice());
            strRecords.add(record.getClosePrice());
            strRecords.add(record.getLowestPrice());
            strRecords.add(record.getHighestPrice());
            ret.add(strRecords);
        }

        return ret;
    }

    @Override
    public void syncAllCompanyFinance(Integer companyStockId) {
        List<CompanyStock> companyStockList = companyStockMapper.selectList(new LambdaQueryWrapper<CompanyStock>());
        for (CompanyStock stock : companyStockList) {
//            if (companyStockId != null && stock.getId() < companyStockId) {
//                continue;
//            }
            List<FinanceReportTimeDTO> reportTimeDTOS = eastMoneyCrawler.getFinanceReport(stock.getStockCode());
            if (CollectionUtils.isNotEmpty(reportTimeDTOS)) {
                int index = 0;
                String dates = "";
                List<FinanceAnalysis> tempFinanceAnalysisDataDTOS = new ArrayList<>();
                for (FinanceReportTimeDTO dto : reportTimeDTOS) {
                    index++;
                    dates = dates + "," + dto.getREPORT_DATE().split(" ")[0];
                    if (index % 5 == 0) {
                        sync(dates, stock, tempFinanceAnalysisDataDTOS);
                        dates = "";
                    }
                    if (index == reportTimeDTOS.size()) {
                        sync(dates, stock, tempFinanceAnalysisDataDTOS);
                    }
                }

                for(FinanceAnalysis financeAnalysis : tempFinanceAnalysisDataDTOS) {
                    financeAnalysis.setTotalAddPercent(getAddPercent(financeAnalysis.getTotalIncome(),
                            getBeforeTotalQuarter(financeAnalysis, tempFinanceAnalysisDataDTOS)));
                    financeAnalysis.setNetProfitPercent(getAddPercent(financeAnalysis.getNetProfit(),
                            getBeforeProfitQuarter(financeAnalysis, tempFinanceAnalysisDataDTOS)));
                    financeAnalysisMapper.updateById(financeAnalysis);
                }
            }
        }
    }

    private void sync(String dates, CompanyStock stock, List<FinanceAnalysis> list) {
        if (StringUtils.isNotBlank(dates)) {
            dates = dates.substring(1, dates.length());
            List<FinanceAnalysisDataDTO> financeAnalysisDataDTOS = eastMoneyCrawler.getFinanceAnalysisData(dates, stock.getStockCode());
            if (CollectionUtils.isNotEmpty(financeAnalysisDataDTOS)) {
                for (FinanceAnalysisDataDTO dataDTO : financeAnalysisDataDTOS) {
                    String reportTime = dataDTO.getREPORT_DATE().split(" ")[0];
                    FinanceAnalysis query = financeAnalysisMapper.selectOne(new LambdaQueryWrapper<FinanceAnalysis>()
                            .eq(FinanceAnalysis::getStockCompanyId, stock.getId())
                            .eq(FinanceAnalysis::getReportTime, reportTime));
                    if (query != null) {
                        continue;
                    }

                    FinanceAnalysis financeAnalysis = new FinanceAnalysis();
                    financeAnalysis.setStockCompanyId(stock.getId());
                    if (dataDTO != null && StringUtils.isNotBlank(dataDTO.getTOTAL_OPERATE_INCOME())) {
                        financeAnalysis.setTotalIncome(new BigDecimal(dataDTO.getTOTAL_OPERATE_INCOME()));

                    }
                    if (dataDTO != null && StringUtils.isNotBlank(dataDTO.getDEDUCT_PARENT_NETPROFIT())) {
                        financeAnalysis.setNetProfit(new BigDecimal(dataDTO.getDEDUCT_PARENT_NETPROFIT()));
                    }
                    BigDecimal netProfit = financeAnalysis.getNetProfit();
                    BigDecimal total = financeAnalysis.getTotalIncome();
                    if(total != null && netProfit != null) {
                        if(netProfit.compareTo(new BigDecimal(0)) > 0  && total.compareTo(new BigDecimal(0)) > 0) {
                            BigDecimal percent = netProfit.divide(total,4, RoundingMode.HALF_UP);
                            financeAnalysis.setProfitTotalPercent(percent.multiply(new BigDecimal(100)));
                        } else {
                            financeAnalysis.setProfitTotalPercent(new BigDecimal(0));
                        }

                    } else {
                        financeAnalysis.setProfitTotalPercent(new BigDecimal(0));
                    }

                    financeAnalysis.setReportTime(reportTime);
                    if (dataDTO.getREPORT_TYPE() != null) {
                        if (FinanceReportTypeEnum.firstQuarter.getName().equals(dataDTO.getREPORT_TYPE())) {
                            financeAnalysis.setReportType(FinanceReportTypeEnum.firstQuarter.getCode());
                        }
                        if (FinanceReportTypeEnum.secondQuarter.getName().equals(dataDTO.getREPORT_TYPE())) {
                            financeAnalysis.setReportType(FinanceReportTypeEnum.secondQuarter.getCode());
                        }
                        if (FinanceReportTypeEnum.thirdQuarter.getName().equals(dataDTO.getREPORT_TYPE())) {
                            financeAnalysis.setReportType(FinanceReportTypeEnum.thirdQuarter.getCode());
                        }
                        if (FinanceReportTypeEnum.annualQuarter.getName().equals(dataDTO.getREPORT_TYPE())) {
                            financeAnalysis.setReportType(FinanceReportTypeEnum.annualQuarter.getCode());
                        }
                    }
                    if (dataDTO != null && StringUtils.isNotBlank(dataDTO.getUPDATE_DATE()) && !dataDTO.getUPDATE_DATE().startsWith("1900-01-01")) {
                        financeAnalysis.setUpdatedTime(LocalDateTime.parse(dataDTO.getUPDATE_DATE(), df));
                    }
                    financeAnalysisMapper.insert(financeAnalysis);
                    list.add(financeAnalysis);
                }
            }
        }
    }

    /**
     * 获取上一年同季度总营收
     *
     * @param now
     * @param list
     * @return
     */
    private BigDecimal getBeforeTotalQuarter(FinanceAnalysis now, List<FinanceAnalysis> list) {
        Integer beforeYear = Integer.valueOf(now.getReportTime().substring(0, 4)) - 1;
        String beforeQuarter = beforeYear + now.getReportTime().substring(4, 10);
        FinanceAnalysis before = null;
        for (FinanceAnalysis dataDTO : list) {
            if (dataDTO.getReportTime().startsWith(beforeQuarter)) {
                before = dataDTO;
                break;
            }
        }
        if (before == null || before.getTotalIncome() == null) {
            return null;
        }
        return before.getTotalIncome();
    }

    /**
     * 获取上一年同季度扣非
     *
     * @param now
     * @param list
     * @return
     */
    private BigDecimal getBeforeProfitQuarter(FinanceAnalysis now, List<FinanceAnalysis> list) {
        Integer beforeYear = Integer.valueOf(now.getReportTime().substring(0, 4)) - 1;
        String beforeQuarter = beforeYear + now.getReportTime().substring(4, 10);
        FinanceAnalysis before = null;
        for (FinanceAnalysis dataDTO : list) {
            if (dataDTO.getReportTime().startsWith(beforeQuarter)) {
                before = dataDTO;
                break;
            }
        }
        if (before == null || before.getNetProfit() == null) {
            return null;
        }
        return before.getNetProfit();
    }

    /**
     * 获取同期的同比增加
     *
     * @param now
     * @param before
     * @return
     */
    private BigDecimal getAddPercent(BigDecimal now, BigDecimal before) {
        if (now == null || before == null) {
            return new BigDecimal(0);
        }
        if(before.compareTo(new BigDecimal(0))==0) {
            return new BigDecimal(100);
        }
        BigDecimal precent = now.divide(before, 4, RoundingMode.HALF_UP);
        BigDecimal addprecent =  precent.subtract(new BigDecimal(1)).multiply(new BigDecimal(100));
        return addprecent;
    }
}
