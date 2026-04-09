package com.dragonchang.strategy.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragonchang.domain.po.BkStock;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.mapper.BkStockMapper;
import com.dragonchang.mapper.CompanyStockMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StrategyUniverseService {

    @Autowired
    private CompanyStockMapper companyStockMapper;

    @Autowired
    private BkStockMapper bkStockMapper;

    public List<CompanyStock> loadUniverse(String universeConfig) {
        LambdaQueryWrapper<CompanyStock> wrapper = new LambdaQueryWrapper<CompanyStock>().eq(CompanyStock::getDeleted, 0);
        if (StringUtils.isBlank(universeConfig)) {
            return companyStockMapper.selectList(wrapper);
        }
        JSONObject config;
        try {
            config = JSON.parseObject(universeConfig);
        } catch (Exception e) {
            return companyStockMapper.selectList(wrapper);
        }
        String type = config.getString("type");
        if (StringUtils.equalsIgnoreCase(type, "BK")) {
            String bkName = config.getString("bkName");
            List<BkStock> bkStocks = bkStockMapper.selectList(new LambdaQueryWrapper<BkStock>()
                    .eq(BkStock::getDeleted, 0)
                    .eq(BkStock::getBkName, bkName));
            List<Integer> ids = bkStocks.stream().map(BkStock::getCompanyStockId).collect(Collectors.toList());
            if (ids.isEmpty()) {
                return new ArrayList<>();
            }
            wrapper.in(CompanyStock::getId, ids);
        } else if (StringUtils.equalsIgnoreCase(type, "IDS")) {
            JSONArray arr = config.getJSONArray("stockIds");
            if (arr != null && !arr.isEmpty()) {
                wrapper.in(CompanyStock::getId, arr.toJavaList(Integer.class));
            }
        }
        return companyStockMapper.selectList(wrapper);
    }
}

