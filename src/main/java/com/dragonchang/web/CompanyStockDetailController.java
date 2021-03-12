package com.dragonchang.web;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dragonchang.domain.enums.HolderTypeEnum;
import com.dragonchang.domain.po.CompanyShareHolder;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.ShareHolderDetail;
import com.dragonchang.service.ICompanyShareHolderService;
import com.dragonchang.service.ICompanyStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String index(Model model, @RequestParam Long companyStockId) {
        CompanyStock stock = companyStockService.getStockById(companyStockId);
        if(stock != null) {
            Map<String, List<ShareHolderDetail>> lTRetMap = new HashMap<>();
            Map<String, List<ShareHolderDetail>> retMap = new HashMap<>();
            List<CompanyShareHolder> result =  companyShareHolderService.getHodlderListByStockId(companyStockId);
            //TODO result 分类排序
            if(CollectionUtils.isNotEmpty(result)) {
                for(CompanyShareHolder holder : result) {
                    if(HolderTypeEnum.GD.getCode().equals(holder.getHolderType())) {
                        List<ShareHolderDetail> shareHolderDetails = companyShareHolderService.getHodlderDetailListByStockId(holder.getId());
                        if(CollectionUtils.isNotEmpty(shareHolderDetails)) {
                            retMap.put(holder.getReportTime(), shareHolderDetails);
                        }
                    } else {
                        List<ShareHolderDetail> shareHolderDetails = companyShareHolderService.getHodlderDetailListByStockId(holder.getId());
                        if(CollectionUtils.isNotEmpty(shareHolderDetails)) {
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
}
