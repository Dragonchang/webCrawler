package com.dragonchang.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.FinanceAnalysisRequestDTO;
import com.dragonchang.domain.dto.FinanceAnalysisResponseDTO;
import com.dragonchang.domain.dto.HolderDetailRequestDTO;
import com.dragonchang.domain.enums.FinanceReportTypeEnum;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.ConceptStock;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.mapper.ConceptStockMapper;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Autowired
    ConceptStockMapper conceptStockMapper;

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
                                        String order, String name, String stockCode, String reportTime,
                                        BigDecimal totalCapitalization, BigDecimal totalAddPercent, BigDecimal netProfitPercent, BigDecimal netProfit,
                                        String bkinfo) {

        if(StringUtils.isNotBlank(stockCode) || StringUtils.isNotBlank(name)) {
            reportTime = null;
        }
        FinanceAnalysisRequestDTO pageRequest = new FinanceAnalysisRequestDTO();
        pageRequest.setName(name);
        pageRequest.setStockCode(stockCode);
        pageRequest.setOrder(order);
        pageRequest.setTotalCapitalization(totalCapitalization);
        pageRequest.setTotalAddPercent(totalAddPercent);
        pageRequest.setNetProfitPercent(netProfitPercent);
        pageRequest.setNetProfit(netProfit);
        pageRequest.setBkinfo(bkinfo);
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
            BigDecimal a = dto.getIncomeTotalPercent();

            if(a != null) {
                BigDecimal b = a.divide(BigDecimal.valueOf(100000000), 2, RoundingMode.HALF_UP);
                dto.setIncomeTotalPercent(b);
            }
            dto.setReportType(FinanceReportTypeEnum.getNameByCode(dto.getReportType()));
            dto.setTotalIncome(ExcelUtil.convertToBillion(dto.getTotalIncome()));
            dto.setNetProfit(ExcelUtil.convertToBillion(dto.getNetProfit()));
            dto.setDtcjje(ExcelUtil.convertToBillion(dto.getDtcjje()));

            List<ConceptStock> conceptStockList = conceptStockMapper.selectList(new LambdaQueryWrapper<ConceptStock>()
                    .eq(ConceptStock::getCompanyStockId, dto.getStockCompanyId()));

            if(conceptStockList != null && !conceptStockList.isEmpty()) {
                String conceptInfo = "";
                for (ConceptStock stock: conceptStockList
                     ) {
                    conceptInfo = conceptInfo + stock.getBkName() + " ";
                }
                dto.setConceptInfo(conceptInfo);
            }
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
