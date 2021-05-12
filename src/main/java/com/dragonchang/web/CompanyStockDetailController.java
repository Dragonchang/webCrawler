package com.dragonchang.web;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dragonchang.domain.dto.HolderCompanyListDTO;
import com.dragonchang.domain.enums.HolderTypeEnum;
import com.dragonchang.domain.po.CompanyShareHolder;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.ShareHolderDetail;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.ICompanyShareHolderService;
import com.dragonchang.service.ICompanyStockService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.IntBuffer;
import java.util.*;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-11 08:58
 **/
@Controller
@RequestMapping("/stockDetail")
public class CompanyStockDetailController {

    @Autowired
    private ICompanyStockService companyStockService;

    @Autowired
    private ICompanyShareHolderService companyShareHolderService;

    @RequestMapping
    public String index(Model model, @RequestParam Integer companyStockId) {
        CompanyStock stock = companyStockService.getStockById(companyStockId);
        if (stock != null) {
            Map<String, List<ShareHolderDetail>> lTRetMap = new LinkedHashMap<>();
            Map<String, List<ShareHolderDetail>> retMap = new LinkedHashMap<>();
            List<CompanyShareHolder> result = companyShareHolderService.getHolderListByStockId(companyStockId);
            if (CollectionUtils.isNotEmpty(result)) {
                for (CompanyShareHolder holder : result) {
                    if (HolderTypeEnum.GD.getCode().equals(holder.getHolderType())) {
                        List<ShareHolderDetail> shareHolderDetails = companyShareHolderService.getHolderDetailListByStockId(holder.getId());
                        if (CollectionUtils.isNotEmpty(shareHolderDetails)) {
                            retMap.put(holder.getReportTime(), shareHolderDetails);
                        }
                    } else {
                        List<ShareHolderDetail> shareHolderDetails = companyShareHolderService.getHolderDetailListByStockId(holder.getId());
                        if (CollectionUtils.isNotEmpty(shareHolderDetails)) {
                            lTRetMap.put(holder.getReportTime(), shareHolderDetails);
                        }
                    }
                }
            }
            model.addAttribute("company", stock);
            model.addAttribute("ltHolder", lTRetMap);
            model.addAttribute("holder", retMap);
        }
        return "stockDetail";
    }

    @RequestMapping(value = "/getDetail")
    public String getDetail(Model model, @RequestParam String name) {
        List<HolderCompanyListDTO> list = companyShareHolderService.getHolderListByName(name);
        model.addAttribute("name", name);
        model.addAttribute("holderList", list);
        return "holderDetail";
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
        for(HolderCompanyListDTO holder : list) {
            if(map.containsKey(holder.getReportTime())) {
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
}
