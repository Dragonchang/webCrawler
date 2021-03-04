package com.dragonchang.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.po.Company;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.ICompanyStockService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-02 17:45
 **/
@Controller
@RequestMapping("/companyStock")
public class CompanyStockController {
    @Autowired
    ICompanyStockService companyStockService;

    @RequestMapping
    public String index(Model model) {
        return "companyStock";
    }

    @GetMapping(value = "/syncStock")
    @ApiOperation(value = "同步公司股票列表信息")
    @ResponseBody
    public JsonResult<IPage<Company>> syncCompanyStock() {
        companyStockService.syncStockListInfo();
        return JsonResult.success();
    }

    @RequestMapping("/pageList")
    @ApiOperation(value = "分页获取关注公司信息")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String name, String stockCode) {
        CompanyStockRequestDTO pageRequest = new CompanyStockRequestDTO();
        pageRequest.setName(name);
        pageRequest.setStockCode(stockCode);
        if (start == 0) {
            start = 1;
        } else {
            double ret = start / length;
            start = (int) Math.floor(ret);
            start = start + 1;
        }
        pageRequest.setPage(start);
        pageRequest.setSize(length);

        IPage<CompanyStock> companyPage = companyStockService.findPage(pageRequest);
        if (companyPage.getTotal() > 0) {
            companyPage.setTotal(companyPage.getTotal() - 1);
        }
        List<CompanyStock> list = companyPage.getRecords();
        int list_count = (int) companyPage.getTotal() + 1;

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count);        // 总记录数
        maps.put("recordsFiltered", list_count);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        return maps;
    }
}
