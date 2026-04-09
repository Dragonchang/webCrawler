package com.dragonchang.strategy.support;

import com.dragonchang.domain.po.CompanyPriceRecord;
import com.dragonchang.mapper.CompanyPriceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyMarketDataService {

    @Autowired
    private CompanyPriceRecordMapper companyPriceRecordMapper;

    public List<CompanyPriceRecord> latestPrices(Integer stockId, Integer days) {
        return companyPriceRecordMapper.selectListByCondition(stockId, null, days);
    }
}

