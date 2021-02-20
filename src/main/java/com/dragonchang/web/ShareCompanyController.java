package com.dragonchang.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-20 13:13
 **/
@Controller
@RequestMapping("/shareCompany")
public class ShareCompanyController {

    /**
     * 获取某个公司的股权穿透
     * @param model
     * @param companyId
     * @return
     */
    @RequestMapping
    public String index(Model model, @RequestParam Long companyId) {
        model.addAttribute("companyId", companyId);
        return "shareCompany";
    }
}
