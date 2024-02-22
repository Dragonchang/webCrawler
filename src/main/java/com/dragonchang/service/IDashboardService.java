package com.dragonchang.service;

import com.dragonchang.domain.vo.JsonResult;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;

public interface IDashboardService {

    JsonResult<Map<String,Object>> avgChartInfo(Date startDate, Date endDate);

    JsonResult<Map<String,Object>> totalChartInfo(Date startDate, Date endDate);

    JsonResult<Map<String, Object>> incomeProfitInfo(String timeSelect);
}
