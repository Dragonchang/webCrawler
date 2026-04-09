package com.dragonchang.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.StrategyResultDTO;
import com.dragonchang.domain.dto.StrategyRunDTO;
import com.dragonchang.domain.dto.StrategyRunPageRequestDTO;
import com.dragonchang.domain.dto.StrategyRunRequestDTO;
import com.dragonchang.domain.po.StrategyRunLog;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.service.IStrategyRunService;
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
@RequestMapping("/strategyRun")
public class StrategyRunController {

    @Autowired
    private IStrategyRunService strategyRunService;

    @RequestMapping
    public String index(Model model) {
        return "strategyRunList";
    }

    @GetMapping("/detailPage")
    public String detailPage(Model model, @RequestParam Long runId) {
        model.addAttribute("runId", runId);
        return "strategyRunDetail";
    }

    @PostMapping("/execute")
    @ResponseBody
    public JsonResult execute(@RequestBody StrategyRunRequestDTO request) {
        return strategyRunService.execute(request);
    }

    @RequestMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        Long strategyId, String strategyName, String runStatus) {
        StrategyRunPageRequestDTO pageRequest = new StrategyRunPageRequestDTO();
        pageRequest.setStrategyId(strategyId);
        pageRequest.setStrategyName(strategyName);
        pageRequest.setRunStatus(runStatus);
        if (start == 0) {
            start = 1;
        } else {
            double ret = start / length;
            start = (int) Math.floor(ret);
            start = start + 1;
        }
        pageRequest.setPage(start);
        pageRequest.setSize(length);
        IPage<StrategyRunDTO> page = strategyRunService.findPage(pageRequest);
        if (page.getTotal() > 0) {
            page.setTotal(page.getTotal() - 1);
        }
        List<StrategyRunDTO> list = page.getRecords();
        int listCount = (int) page.getTotal() + 1;
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", listCount);
        maps.put("recordsFiltered", listCount);
        maps.put("data", list);
        return maps;
    }

    @GetMapping("/detail")
    @ResponseBody
    public JsonResult<StrategyRunDTO> detail(@RequestParam Long runId) {
        return JsonResult.success(strategyRunService.getDetail(runId));
    }

    @GetMapping("/logList")
    @ResponseBody
    public JsonResult<List<StrategyRunLog>> logList(@RequestParam Long runId) {
        return JsonResult.success(strategyRunService.getLogs(runId));
    }

    @GetMapping("/resultList")
    @ResponseBody
    public JsonResult<List<StrategyResultDTO>> resultList(@RequestParam Long runId) {
        return JsonResult.success(strategyRunService.getResults(runId));
    }
}

