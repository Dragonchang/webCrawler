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
        List<Integer> count = new ArrayList<Integer>();
        List<IncomeProfitListDTO> list = financeAnalysisMapper.getIncomeAndProfit(timeSelect);
        String tempData = "";
        Integer tempCount = 0;
        BigDecimal tempTotalIncome = new BigDecimal(0);
        BigDecimal tempNetProfit =  new BigDecimal(0);

        for (IncomeProfitListDTO incomeProfitListDTO : list  ) {
            if(!tempData.equals(incomeProfitListDTO.getReportTime())) {
                if(tempCount != 0) {
                    count.add(tempCount);
                    tempCount = 0;
                }
                if(!tempTotalIncome.equals(0)) {
                    incomeList.add(ExcelUtil.convertToBillion(tempTotalIncome));
                    tempTotalIncome = new BigDecimal(0);
                }
                if(!tempNetProfit.equals(0)) {
                    profitList.add(ExcelUtil.convertToBillion(tempNetProfit));
                    tempNetProfit =  new BigDecimal(0);
                }
                tempData = incomeProfitListDTO.getReportTime();
                dayList.add(tempData);
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
        if(tempCount != 0) {
            count.add(tempCount);
        }
        result.put("dayList", dayList);
        result.put("incomeList", incomeList);
        result.put("profitList", profitList);
        result.put("count", count);
        return JsonResult.success(result);
    }
}
