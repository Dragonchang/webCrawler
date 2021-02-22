package com.dragonchang.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.ShareStructureDTO;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.dto.tyc.ShareStructureRequestDto;
import com.dragonchang.domain.po.Company;
import com.dragonchang.domain.po.ShareStructure;
import com.dragonchang.service.ICompanyService;
import com.dragonchang.service.IShareStructureService;
import com.dragonchang.util.DateUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-20 13:13
 **/
@Controller
@RequestMapping("/shareCompany")
public class ShareCompanyController {

    @Autowired
    ICompanyService companyService;

    @Autowired
    IShareStructureService shareStructureService;
    /**
     * 获取某个公司的股权穿透
     * @param model
     * @param companyId
     * @return
     */
    @RequestMapping
    public String index(Model model,
                        @RequestParam Long companyId,
                        @RequestParam(defaultValue = "1") Integer orderByAmount,
                        @RequestParam(required = false) Integer orderByPercent,
                        @RequestParam(required = false) String filterTime) {
        Company company = companyService.getCompanyById(companyId);
        ShareStructureRequestDto requestDto = new ShareStructureRequestDto();
        requestDto.setCompanyId(companyId);
        // parse param
        if (filterTime!=null && filterTime.trim().length()>0) {
            String[] temp = filterTime.split(" - ");
            if (temp.length == 2) {
                requestDto.setStartTime(DateUtil.dateTimeToLocal(DateUtil.parseDateTime(temp[0])));
                requestDto.setEndTime(DateUtil.dateTimeToLocal(DateUtil.parseDateTime(temp[1])));
            }
        } else {
            //获取最近一天的信息
            requestDto.setStartTime(DateUtil.getCurrentStartTime());
            requestDto.setEndTime(DateUtil.getCurrentEndTime());
        }
        List<ShareStructureDTO> shareStructures = shareStructureService.getShareStructure(requestDto);
        for(ShareStructureDTO shareStructureDTO : shareStructures) {
            shareStructureDTO.setAmount(getAmount(shareStructureDTO.getShareCompanyAmount()));
            shareStructureDTO.setPercent(getPercent(shareStructureDTO.getShareCompanyPercent()));
        }

        Collections.sort(shareStructures, new Comparator<ShareStructureDTO>() {
            @Override
            public int compare(ShareStructureDTO o1, ShareStructureDTO o2) {
                //按照金额排序
                if(orderByPercent == null) {
                    return o2.getAmount().compareTo(o1.getAmount());
                } else {
                 //按比例排序
                    return o2.getPercent().compareTo(o1.getPercent());
                }
            }
        });
        model.addAttribute("company", company);
        model.addAttribute("data", shareStructures);
        return "shareCompany";
    }

    /**
     * 将2.99亿、2533.36万转化为亿为单位的小数
     * @param amount
     * @return
     */
    private BigDecimal getAmount(String amount) {
        if(StringUtils.isEmpty(amount)) {
            return new BigDecimal(0);
        }
        BigDecimal ret = null;
        if(amount.endsWith("万")) {
            amount = amount.substring(0, amount.length()-1);
            ret = new BigDecimal(amount).divide(new BigDecimal(10000), 6, BigDecimal.ROUND_HALF_UP);
        }
        if(amount.endsWith("亿")) {
            amount = amount.substring(0, amount.length()-1);
            ret = new BigDecimal(amount);
        }
        return ret;
    }

    /**
     * 去除%
     * @param shareCompanyPercent
     * @return
     */
    private BigDecimal getPercent(String shareCompanyPercent) {
        if(StringUtils.isEmpty(shareCompanyPercent)) {
            return new BigDecimal(0);
        }
        BigDecimal percent = new BigDecimal(shareCompanyPercent.substring(0, shareCompanyPercent.length()-1));
        return percent;
    }
}
