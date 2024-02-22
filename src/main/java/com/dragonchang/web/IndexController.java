package com.dragonchang.web;

import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.IDashboardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;


/**
 * index controller
 *
 */
@Controller
public class IndexController {
    @Autowired
    IDashboardService dashboardService;

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/avgChartInfo")
    @ApiOperation(value = "获取平均股价统计信息")
    @ResponseBody
    public JsonResult<Map<String, Object>> avgChartInfo(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        JsonResult<Map<String, Object>> chartInfo = dashboardService.avgChartInfo(startDate, endDate);
        return chartInfo;
    }

    @RequestMapping("/totalChartInfo")
    @ApiOperation(value = "获取总市值统计信息")
    @ResponseBody
    public JsonResult<Map<String, Object>> totalChartInfo(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        JsonResult<Map<String, Object>> chartInfo = dashboardService.totalChartInfo(startDate, endDate);
        return chartInfo;
    }

    @RequestMapping("/incomeProfit")
    @ApiOperation(value = "企业营收利润统计信息")
    @ResponseBody
    public JsonResult<Map<String, Object>> incomeProfitInfo(String timeSelect) {
        JsonResult<Map<String, Object>> chartInfo = dashboardService.incomeProfitInfo(timeSelect);
        return chartInfo;
    }
}
