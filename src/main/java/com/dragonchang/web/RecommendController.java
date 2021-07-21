package com.dragonchang.web;

import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.IUpwardTrendService;
import com.dragonchang.util.DateUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-11 14:43
 **/
@Controller
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    IUpwardTrendService upwardTrendService;


    @RequestMapping
    public String index(Model model) {
        return "recommend";
    }

    @GetMapping(value = "/syncUpwardTrend")
    @ApiOperation(value = "生成上涨趋势公司列表")
    @ResponseBody
    public JsonResult syncUpwardTrend(@RequestParam(required = false) String today) {
        upwardTrendService.generateUpwardTrendListByToday(today);
        return JsonResult.success();
    }
}
