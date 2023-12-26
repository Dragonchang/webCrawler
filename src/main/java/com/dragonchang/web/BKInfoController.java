package com.dragonchang.web;

import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.IBKInfoService;
import com.dragonchang.service.ICompanyFinanceAnalysisService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2023-12-06 16:44
 **/
@Controller
@RequestMapping("/bkInfo")
public class BKInfoController {
    @Autowired
    IBKInfoService bKInfoService;


    @RequestMapping("/updateBKInfo")
    @ApiOperation(value = "更新板块信息")
    public JsonResult updateBKInfo() {
         bKInfoService.updateBKInfo();
        return JsonResult.success();
    }

    @RequestMapping("/updateConceptInfo")
    @ApiOperation(value = "更新概念板块信息")
    public JsonResult updateConceptInfo() {
        bKInfoService.updateConceptInfo();
        return JsonResult.success();
    }
}
