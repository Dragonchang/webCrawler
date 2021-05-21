package com.dragonchang.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.FinanceAnalysisRequestDTO;
import com.dragonchang.domain.dto.FinanceAnalysisResponseDTO;
import com.dragonchang.domain.dto.HolderDetailRequestDTO;
import com.dragonchang.domain.enums.FinanceReportTypeEnum;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.service.ICompanyFinanceAnalysisService;
import com.dragonchang.util.ExcelUtil;
import com.dragonchang.util.HttpUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-19 16:57
 **/
@Controller
@RequestMapping("/financeAnalysis")
public class CompanyFinanceAnalysisController {

    @Autowired
    ICompanyFinanceAnalysisService companyFinanceAnalysisService;

    @RequestMapping()
    public String index(Model model) {
        //上报年度列表
        List<String> reportTime = companyFinanceAnalysisService.getTotalReportTimeList();
        model.addAttribute("reportTime", reportTime);
        return "financeAnalysis";
    }

    @RequestMapping("/pageList")
    @ApiOperation(value = "分页获取股份公司信息")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String order, String name, String stockCode, String reportTime) {

        if(StringUtils.isNotBlank(stockCode) || StringUtils.isNotBlank(name)) {
            reportTime = null;
        }
        FinanceAnalysisRequestDTO pageRequest = new FinanceAnalysisRequestDTO();
        pageRequest.setName(name);
        pageRequest.setStockCode(stockCode);
        pageRequest.setOrder(order);
        if(!StringUtils.isEmpty(reportTime)) {
            pageRequest.setReportTime(reportTime);
        }
        if (start == 0) {
            start = 1;
        } else {
            double ret = start / length;
            start = (int) Math.floor(ret);
            start = start + 1;
        }
        pageRequest.setPage(start);
        pageRequest.setSize(length);

        IPage<FinanceAnalysisResponseDTO> companyPage = companyFinanceAnalysisService.findPage(pageRequest);
        if (companyPage.getTotal() > 0) {
            companyPage.setTotal(companyPage.getTotal() - 1);
        }
        List<FinanceAnalysisResponseDTO> list = companyPage.getRecords();
        for(FinanceAnalysisResponseDTO dto : list) {
            dto.setReportType(FinanceReportTypeEnum.getNameByCode(dto.getReportType()));
            dto.setTotalIncome(ExcelUtil.convertToBillion(dto.getTotalIncome()));
            dto.setNetProfit(ExcelUtil.convertToBillion(dto.getNetProfit()));
        }
        int list_count = (int) companyPage.getTotal() + 1;

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count);        // 总记录数
        maps.put("recordsFiltered", list_count);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        return maps;
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出财务信息")
    @ResponseBody
    public ResponseEntity<byte[]> exportFlow(@RequestBody FinanceAnalysisRequestDTO request) {
        ExcelData data = companyFinanceAnalysisService.exportFlow(request);
        return HttpUtil.generateHttpEntity(ExcelUtil.readDataAsByteArray(data), data.getFileName(), ".xlsx");
    }
}
