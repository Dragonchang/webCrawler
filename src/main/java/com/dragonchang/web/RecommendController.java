package com.dragonchang.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-11 14:43
 **/
@Controller
@RequestMapping("/recommend")
public class RecommendController {
    @RequestMapping
    public String index(Model model) {
        return "recommend";
    }
}
