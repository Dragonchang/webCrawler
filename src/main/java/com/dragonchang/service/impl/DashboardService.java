package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dragonchang.domain.dto.IncomeProfitListDTO;
import com.dragonchang.domain.po.TotalStockRecord;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.mapper.FinanceAnalysisMapper;
import com.dragonchang.mapper.TotalStockRecordMapper;
import com.dragonchang.service.IDashboardService;
import com.dragonchang.util.DateUtil;
import com.dragonchang.util.ExcelUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-08 14:42
 **/
@Slf4j
@Service
public class DashboardService implements IDashboardService {

    @Autowired
    private TotalStockRecordMapper totalStockRecordMapper;

    @Autowired
    private FinanceAnalysisMapper financeAnalysisMapper;

    @Override
    public JsonResult<Map<String, Object>> avgChartInfo(Date startDate, Date endDate) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> dayList = new ArrayList<String>();
        List<Double> avgPriceList = new ArrayList<Double>();
        List<TotalStockRecord> recordList = totalStockRecordMapper.seletListByTime(startDate, endDate);
        if(CollectionUtils.isNotEmpty(recordList)) {
            for(TotalStockRecord record : recordList) {
                dayList.add(DateUtil.formatLocalDateTime(record.getRecordTime()));
                avgPriceList.add(record.getAveragePrice().doubleValue());
            }
            result.put("dayList", dayList);
            result.put("avgPriceList", avgPriceList);
        }
        return JsonResult.success(result);
    }

    @Override
    public JsonResult<Map<String, Object>> totalChartInfo(Date startDate, Date endDate) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> dayList = new ArrayList<String>();
        List<Double> totalCapitalizationList = new ArrayList<Double>();
        List<Double> lastCirculation = new ArrayList<Double>();
        List<TotalStockRecord> recordList = totalStockRecordMapper.seletListByTime(startDate, endDate);
        if(CollectionUtils.isNotEmpty(recordList)) {
            for(TotalStockRecord record : recordList) {
                dayList.add(DateUtil.formatLocalDateTime(record.getRecordTime()));
                totalCapitalizationList.add(record.getTotalCapitalization().doubleValue());
                lastCirculation.add(record.getLastCirculation().doubleValue());
            }
            result.put("dayList", dayList);
            result.put("totalCapitalizationList", totalCapitalizationList);
            result.put("lastCirculation", lastCirculation);
        }
        return JsonResult.success(result);
    }


    @Override
    public JsonResult<Map<String, Object>> incomeProfitInfo(String timeSelect) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> dayList = new ArrayList<String>();
        List<BigDecimal> incomeList = new ArrayList<BigDecimal>();
        List<BigDecimal> profitList = new ArrayList<BigDecimal>();
        List<BigDecimal> profitRateList = new ArrayList<BigDecimal>();
        List<Integer> count = new ArrayList<Integer>();
        List<IncomeProfitListDTO> list = financeAnalysisMapper.getIncomeAndProfit(timeSelect);
        String tempData = "";
        Integer tempCount = 0;
        BigDecimal tempTotalIncome = new BigDecimal(0);
        BigDecimal tempNetProfit =  new BigDecimal(0);
        if(!list.isEmpty()) {
            tempData = list.get(0).getReportTime();
        }
        for (IncomeProfitListDTO incomeProfitListDTO : list  ) {
            if(!tempData.equals(incomeProfitListDTO.getReportTime())) {
                log.warn("tempTotalIncome: "+tempTotalIncome+" tempNetProfit: "+tempNetProfit
                        +" tempCount:" +tempCount+ " tempData: "+tempData);
                if(tempCount != 0) {
                    count.add(tempCount);
                    tempCount = 0;
                }
                if(tempTotalIncome.compareTo(BigDecimal.ZERO)!=0 && tempNetProfit.compareTo(BigDecimal.ZERO)!=0)
                {

                    BigDecimal rate =  ExcelUtil.convertToBillion(tempNetProfit).divide(ExcelUtil.convertToBillion(tempTotalIncome), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                    log.warn("rate: "+rate+" tempData: "+tempData);
                    profitRateList.add(rate);
                } else {
                    profitRateList.add(new BigDecimal(0));
                }
                if(tempTotalIncome.compareTo(BigDecimal.ZERO)!=0) {
                    incomeList.add(ExcelUtil.convertToBillion(tempTotalIncome));
                    log.warn("tempTotalIncome: "+tempTotalIncome+" tempData: "+tempData);
                    tempTotalIncome = new BigDecimal(0);
                } else {
                    incomeList.add(new BigDecimal(0));
                }
                if(tempNetProfit.compareTo(BigDecimal.ZERO)!=0) {
                    profitList.add(ExcelUtil.convertToBillion(tempNetProfit));
                    log.warn("tempNetProfit: "+tempNetProfit+" tempData: "+tempData);
                    tempNetProfit =  new BigDecimal(0);
                } else {
                    profitList.add(new BigDecimal(0));
                }
                dayList.add(tempData);
                tempData = incomeProfitListDTO.getReportTime();
                log.warn(" tempData: "+tempData);
                tempCount ++;
                if(incomeProfitListDTO.getTotalIncome() != null) {
                    tempTotalIncome = tempTotalIncome.add(incomeProfitListDTO.getTotalIncome());
                }
                if(incomeProfitListDTO.getNetProfit() != null) {
                    tempNetProfit = tempNetProfit.add(incomeProfitListDTO.getNetProfit());
                }
            } else {
                tempCount ++;
                if(incomeProfitListDTO.getTotalIncome() != null) {
                    tempTotalIncome = tempTotalIncome.add(incomeProfitListDTO.getTotalIncome());
                }
                if(incomeProfitListDTO.getNetProfit() != null) {
                    tempNetProfit = tempNetProfit.add(incomeProfitListDTO.getNetProfit());
                }
            }
        }
        //添加最后一个时间记录
        if(tempCount != 0) {
            count.add(tempCount);
        }
        if(tempTotalIncome.compareTo(BigDecimal.ZERO)!=0 && tempNetProfit.compareTo(BigDecimal.ZERO)!=0)
        {
            BigDecimal rate =  ExcelUtil.convertToBillion(tempNetProfit).divide(ExcelUtil.convertToBillion(tempTotalIncome), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            log.warn("rate: "+rate+" tempData: "+tempData);
            profitRateList.add(rate);
        } else {
            profitRateList.add(new BigDecimal(0));
        }
        if(tempTotalIncome.compareTo(BigDecimal.ZERO)!=0) {
            incomeList.add(ExcelUtil.convertToBillion(tempTotalIncome));
            log.warn("tempTotalIncome: "+tempTotalIncome+" tempData: "+tempData);
            tempTotalIncome = new BigDecimal(0);
        } else {
            incomeList.add(new BigDecimal(0));
        }
        if(tempNetProfit.compareTo(BigDecimal.ZERO)!=0) {
            profitList.add(ExcelUtil.convertToBillion(tempNetProfit));
            log.warn("tempNetProfit: "+tempNetProfit+" tempData: "+tempData);
            tempNetProfit =  new BigDecimal(0);
        } else {
            profitList.add(new BigDecimal(0));
        }
        dayList.add(tempData);

        result.put("dayList", dayList);
        result.put("incomeList", incomeList);
        result.put("profitList", profitList);
        result.put("profitRateList", profitRateList);
        result.put("count", count);
        return JsonResult.success(result);
    }
}
