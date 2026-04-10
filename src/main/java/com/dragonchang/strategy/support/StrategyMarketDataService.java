package com.dragonchang.strategy.support;

import com.dragonchang.domain.po.CompanyPriceRecord;
import com.dragonchang.mapper.CompanyPriceRecordMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class StrategyMarketDataService {

    @Autowired
    private CompanyPriceRecordMapper companyPriceRecordMapper;

    public List<CompanyPriceRecord> latestPrices(Integer stockId, Integer days) {
        return companyPriceRecordMapper.selectListByCondition(stockId, null, days);
    }

    public BigDecimal movingAverage(Integer stockId, Integer days) {
        if (stockId == null || days == null || days <= 0) {
            return null;
        }
        List<CompanyPriceRecord> records = latestPrices(stockId, days);
        if (records == null || records.isEmpty()) {
            return null;
        }
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        for (CompanyPriceRecord record : records) {
            if (record == null || StringUtils.isBlank(record.getClosePrice())) {
                continue;
            }
            try {
                total = total.add(new BigDecimal(record.getClosePrice().trim()));
                count++;
            } catch (Exception ignored) {
            }
        }
        if (count == 0) {
            return null;
        }
        return total.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP);
    }
}
