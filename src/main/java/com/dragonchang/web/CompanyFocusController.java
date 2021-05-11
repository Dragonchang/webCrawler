package com.dragonchang.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.FocusAddRequestDTO;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.enums.FocusTypeEnum;
import com.dragonchang.domain.po.Focus;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.IFocusService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 * @create: 2021-02-18 11:36
 **/
@Controller
@RequestMapping("/companyFocus")
public class CompanyFocusController {

    @Autowired
    IFocusService focusService;

    @RequestMapping
    public String index(Model model) {
        return "companyFocus";
    }

    @RequestMapping("/pageList")
    @ApiOperation(value = "分页获取关注公司信息")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String type, String companyName, String stockCode) {
        CompanyRequestDTO pageRequest = new CompanyRequestDTO();
        pageRequest.setCompanyName(companyName);
        pageRequest.setStockCode(stockCode);
        pageRequest.setType(type);
        if (start == 0) {
            start = 1;
        } else {
            double ret = start / length;
            start = (int) Math.floor(ret);
            start = start + 1;
        }
        pageRequest.setPage(start);
        pageRequest.setSize(length);

        IPage<Focus> companyPage = focusService.findPage(pageRequest);
        if (companyPage.getTotal() > 0) {
            companyPage.setTotal(companyPage.getTotal() - 1);
        }
        List<Focus> list = companyPage.getRecords();
        for(Focus focus: list) {
            focus.setType(FocusTypeEnum.getNameByCode(focus.getType()));
        }
        int list_count = (int) companyPage.getTotal() + 1;

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count);        // 总记录数
        maps.put("recordsFiltered", list_count);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        return maps;
    }

    @RequestMapping("/delete")
    @ApiOperation(value = "删除关注的公司")
    @ResponseBody
    public JsonResult delete(@RequestParam int id) {
        return focusService.delete(id);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加关注的公司")
    @ResponseBody
    public JsonResult add(@RequestBody FocusAddRequestDTO addRequestDTO) {
        focusService.add(addRequestDTO);
        return JsonResult.success();
    }
}
