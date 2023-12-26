package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dragonchang.domain.dto.IncomeProfitListDTO;
import com.dragonchang.domain.po.TotalStockRecord;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.mapper.FinanceAnalysisMapper;
import com.dragonchang.mapper.TotalStockRecordMapper;
import com.dragonchang.service.IDashboardService;
import com.dragonchang.util.DateUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

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
    public JsonResult<Map<String, Object>> incomeProfitInfo(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> dayList = new ArrayList<String>();
        List<Double> incomeList = new ArrayList<Double>();
        List<Double> profitList = new ArrayList<Double>();
        List<Integer> count = new ArrayList<Integer>();
        List<IncomeProfitListDTO> list = financeAnalysisMapper.getIncomeAndProfit("4");

        count.add(1);
        dayList.add("2012年2月3日");
        incomeList.add(1293849.333);
        profitList.add(33393904.00);
        result.put("dayList", dayList);
        result.put("incomeList", incomeList);
        result.put("profitList", profitList);
        result.put("count", count);
        return JsonResult.success(result);
    }
}
