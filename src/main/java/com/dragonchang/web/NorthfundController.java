package com.dragonchang.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/northfund")
public class NorthfundController {
    @RequestMapping()
    public String index(Model model) {
        return "northfund";
    }

}
