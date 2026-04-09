package com.dragonchang.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.StrategyRequestDTO;
import com.dragonchang.domain.dto.StrategySaveRequestDTO;
import com.dragonchang.domain.po.StrategyInfo;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.IStrategyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/strategy")
public class StrategyController {

    @Autowired
    private IStrategyService strategyService;

    @RequestMapping
    public String index(Model model) {
        return "strategyList";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam(required = false) Long id) {
        if (id != null) {
            model.addAttribute("strategy", strategyService.getById(id));
        }
        return "strategyEdit";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @ApiOperation(value = "分页获取策略列表")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String strategyName, String strategyCode, String category, Integer status) {
        StrategyRequestDTO pageRequest = new StrategyRequestDTO();
        pageRequest.setStrategyName(strategyName);
        pageRequest.setStrategyCode(strategyCode);
        pageRequest.setCategory(category);
        if (status != null && status >= 0) {
            pageRequest.setStatus(status);
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
        IPage<StrategyInfo> page = strategyService.findPage(pageRequest);
        if (page.getTotal() > 0) {
            page.setTotal(page.getTotal() - 1);
        }
        List<StrategyInfo> list = page.getRecords();
        int listCount = (int) page.getTotal() + 1;
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", listCount);
        maps.put("recordsFiltered", listCount);
        maps.put("data", list);
        return maps;
    }

    @GetMapping("/detail")
    @ResponseBody
    public JsonResult<StrategyInfo> detail(@RequestParam Long id) {
        return JsonResult.success(strategyService.getById(id));
    }

    @PostMapping("/save")
    @ResponseBody
    public JsonResult save(@RequestBody StrategySaveRequestDTO request) {
        return strategyService.saveStrategy(request);
    }

    @PostMapping("/publish")
    @ResponseBody
    public JsonResult publish(@RequestParam Long id) {
        return strategyService.publish(id);
    }

    @PostMapping("/changeStatus")
    @ResponseBody
    public JsonResult changeStatus(@RequestParam Long id, @RequestParam Integer status) {
        return strategyService.changeStatus(id, status);
    }
}

