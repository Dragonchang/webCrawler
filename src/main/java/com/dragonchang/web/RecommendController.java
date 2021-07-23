package com.dragonchang.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.NewUpwardTrendDTO;
import com.dragonchang.domain.dto.UpwardTrendDTO;
import com.dragonchang.domain.dto.UpwardTrendPageRequestDTO;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.NewUpwardTrend;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.IUpwardTrendService;
import com.dragonchang.util.DateUtil;
import com.dragonchang.util.ExcelUtil;
import com.dragonchang.util.HttpUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/syncNewUpwardTrend")
    @ApiOperation(value = "生成上涨趋势公司列表")
    @ResponseBody
    public JsonResult syncNewUpwardTrend(@RequestParam(required = false) String today) {
        upwardTrendService.generateNewUpwardTrendListByToday(today);
        return JsonResult.success();
    }

    @RequestMapping("/pageList")
    @ApiOperation(value = "分页获当日趋势信息")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String name, String stockCode, String filter, String isHeight, String today) {
        UpwardTrendPageRequestDTO pageRequest = new UpwardTrendPageRequestDTO();
        if (start == 0) {
            start = 1;
        } else {
            double ret = start / length;
            start = (int) Math.floor(ret);
            start = start + 1;
        }
        if(StringUtils.isBlank(today)) {
            today = DateUtil.formatDate(new Date());
        }
        pageRequest.setToday(today);
        pageRequest.setName(name);
        pageRequest.setStockCode(stockCode);
        if(StringUtils.isNotBlank(filter) && !filter.equals("1")) {
            pageRequest.setFilter(filter);
        }
        if(StringUtils.isNotBlank(isHeight) && !isHeight.equals("1")) {
            pageRequest.setIsHeight(isHeight);
        }
        pageRequest.setPage(start);
        pageRequest.setSize(length);

        IPage<UpwardTrendDTO> upwardTrendPage = upwardTrendService.findPage(pageRequest);
        if (upwardTrendPage.getTotal() > 0) {
            upwardTrendPage.setTotal(upwardTrendPage.getTotal() - 1);
        }
        List<UpwardTrendDTO> list = upwardTrendPage.getRecords();
        int list_count = (int) upwardTrendPage.getTotal() + 1;

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count);        // 总记录数
        maps.put("recordsFiltered", list_count);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        return maps;
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出公司信息")
    @ResponseBody
    public ResponseEntity<byte[]> exportFlow(@RequestBody UpwardTrendPageRequestDTO pageRequest) {
        if(StringUtils.isBlank(pageRequest.getToday())) {
            String today = DateUtil.formatDate(new Date());
            pageRequest.setToday(today);
        }
        if(StringUtils.isBlank(pageRequest.getFilter()) || pageRequest.getFilter().equals("1")) {
            pageRequest.setFilter(null);
        }
        if(StringUtils.isBlank(pageRequest.getIsHeight()) || pageRequest.getIsHeight().equals("1")) {
            pageRequest.setIsHeight(null);
        }
        ExcelData data = upwardTrendService.exportFlow(pageRequest);
        return HttpUtil.generateHttpEntity(ExcelUtil.readDataAsByteArray(data), data.getFileName(), ".xlsx");
    }


    @RequestMapping("/newPageList")
    @ApiOperation(value = "分页获当日趋势信息")
    @ResponseBody
    public Map<String, Object> newPageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String name, String stockCode, String filter, String isHeight, String today) {

        UpwardTrendPageRequestDTO pageRequest = new UpwardTrendPageRequestDTO();
        if (start == 0) {
            start = 1;
        } else {
            double ret = start / length;
            start = (int) Math.floor(ret);
            start = start + 1;
        }
        if(StringUtils.isBlank(today)) {
            today = DateUtil.formatDate(new Date());
        }
        pageRequest.setToday(today);
        pageRequest.setName(name);
        pageRequest.setStockCode(stockCode);
        if(StringUtils.isNotBlank(filter) && !filter.equals("1")) {
            pageRequest.setFilter(filter);
        }
        if(StringUtils.isNotBlank(isHeight) && !isHeight.equals("1")) {
            pageRequest.setIsHeight(isHeight);
        }
        pageRequest.setPage(start);
        pageRequest.setSize(length);

        IPage<NewUpwardTrendDTO> upwardTrendPage = upwardTrendService.newFindPage(pageRequest);
        if (upwardTrendPage.getTotal() > 0) {
            upwardTrendPage.setTotal(upwardTrendPage.getTotal() - 1);
        }
        List<NewUpwardTrendDTO> list = upwardTrendPage.getRecords();
        int list_count = (int) upwardTrendPage.getTotal() + 1;

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count);        // 总记录数
        maps.put("recordsFiltered", list_count);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        return maps;

    }

    @PostMapping(value = "/newExport")
    @ApiOperation(value = "导出公司信息")
    @ResponseBody
    public ResponseEntity<byte[]> newExportFlow(@RequestBody UpwardTrendPageRequestDTO pageRequest) {
        if(StringUtils.isBlank(pageRequest.getToday())) {
            String today = DateUtil.formatDate(new Date());
            pageRequest.setToday(today);
        }
        if(StringUtils.isBlank(pageRequest.getFilter()) || pageRequest.getFilter().equals("1")) {
            pageRequest.setFilter(null);
        }
        if(StringUtils.isBlank(pageRequest.getIsHeight()) || pageRequest.getIsHeight().equals("1")) {
            pageRequest.setIsHeight(null);
        }
        ExcelData data = upwardTrendService.newExportFlow(pageRequest);
        return HttpUtil.generateHttpEntity(ExcelUtil.readDataAsByteArray(data), data.getFileName(), ".xlsx");
    }

}
