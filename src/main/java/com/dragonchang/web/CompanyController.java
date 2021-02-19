package com.dragonchang.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.po.Company;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.ICompanyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-18 11:36
 **/
@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    ICompanyService companyService;

    @RequestMapping
    public String index(Model model) {
        return "company";
    }

    @PostMapping(value = "/findPage")
    @ApiOperation(value = "分页获取设备信息")
    public JsonResult<IPage<Company>> findPage(@RequestBody CompanyRequestDTO pageRequest) {
        companyService.syncShareInfoWithCompanyId(1L);
        return JsonResult.success(companyService.findPage(pageRequest));
    }
}
