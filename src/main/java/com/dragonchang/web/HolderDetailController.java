package com.dragonchang.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.HolderCompanyListDTO;
import com.dragonchang.domain.dto.HolderDetailRequestDTO;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.ICompanyShareHolderService;
import com.dragonchang.util.ExcelUtil;
import com.dragonchang.util.HttpUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @program: webcrawler
 * @description: 持有信息穿透
 * @author: zhangfl
 * @create: 2021-05-14 15:16
 **/
@Controller
@RequestMapping("/holderDetail")
public class HolderDetailController {
    @Autowired
    private ICompanyShareHolderService companyShareHolderService;

    @RequestMapping(value = "/getDetail")
    public String getDetail(Model model, @RequestParam String name) {
        List<HolderCompanyListDTO> list = companyShareHolderService.getHolderListByName(name);
        //上报季度
        List<String> reportTime = new ArrayList<>();
        //每个季度对象持有公司的计数
        List<Integer> holderCount = new ArrayList<>();

        TreeMap<String, Integer> map = new TreeMap<String, Integer>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for (HolderCompanyListDTO holder : list) {
            if (map.containsKey(holder.getReportTime())) {
                Integer value = map.get(holder.getReportTime());
                map.put(holder.getReportTime(), value + 1);
            } else {
                map.put(holder.getReportTime(), 1);
            }
        }
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            reportTime.add(key);
            holderCount.add(map.get(key));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        model.addAttribute("reportTime", reportTime);
        model.addAttribute("name", name);
        return "holderDetail";
    }

    @RequestMapping("/pageList")
    @ApiOperation(value = "分页获取持有穿透信息")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String name, String companyName, String companyStock,
                                        int count, String reportTime) {
        HolderDetailRequestDTO pageRequest = new HolderDetailRequestDTO();
        pageRequest.setCompanyName(companyName);
        pageRequest.setCompanyStock(companyStock);
        if(count != 0) {
            pageRequest.setCount(count);
        }
        if(reportTime != null && !reportTime.equals("0")) {
            pageRequest.setReportTime(reportTime);
        }
        pageRequest.setName(name);
        if (start == 0) {
            start = 1;
        } else {
            double ret = start / length;
            start = (int) Math.floor(ret);
            start = start + 1;
        }
        pageRequest.setPage(start);
        pageRequest.setSize(length);

        IPage<HolderCompanyListDTO> holderDetailPage = companyShareHolderService.findPage(pageRequest);
        if (holderDetailPage.getTotal() > 0) {
            holderDetailPage.setTotal(holderDetailPage.getTotal() - 1);
        }
        List<HolderCompanyListDTO> list = holderDetailPage.getRecords();
        int list_count = (int) holderDetailPage.getTotal() + 1;
        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count);        // 总记录数
        maps.put("recordsFiltered", list_count);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        return maps;
    }

    @RequestMapping("/countChartInfo")
    @ApiOperation(value = "获取每个季度持有个数的统计")
    @ResponseBody
    public JsonResult<Map<String, Object>> countChartInfo(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate,
                                                          @RequestParam String name) {
        List<HolderCompanyListDTO> list = companyShareHolderService.getHolderListByName(name);
        //上报季度
        List<String> reportTime = new ArrayList<>();
        //每个季度对象持有公司的计数
        List<Integer> holderCount = new ArrayList<>();

        TreeMap<String, Integer> map = new TreeMap<String, Integer>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for (HolderCompanyListDTO holder : list) {
            if (map.containsKey(holder.getReportTime())) {
                Integer value = map.get(holder.getReportTime());
                map.put(holder.getReportTime(), value + 1);
            } else {
                map.put(holder.getReportTime(), 1);
            }
        }
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            reportTime.add(key);
            holderCount.add(map.get(key));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("reportTime", reportTime);
        result.put("holderCount", holderCount);
        return JsonResult.success(result);
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出持有记录")
    @ResponseBody
    public ResponseEntity<byte[]> exportFlow(@RequestBody  HolderDetailRequestDTO request) {
        if(request.getCount() == 0) {
            request.setCount(null);
        }
        if(request.getReportTime() != null && request.getReportTime().equals("0")) {
            request.setReportTime(null);
        }
        ExcelData data = companyShareHolderService.exportFlow(request);
        return HttpUtil.generateHttpEntity(ExcelUtil.readDataAsByteArray(data), data.getFileName(), ".xlsx");
    }
}
