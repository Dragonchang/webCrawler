package com.dragonchang.service;

import com.dragonchang.domain.vo.JsonResult;

import java.util.Date;
import java.util.Map;

public interface IDashboardService {

    public JsonResult<Map<String,Object>> avgChartInfo(Date startDate, Date endDate);

    public JsonResult<Map<String,Object>> totalChartInfo(Date startDate, Date endDate);
}
