package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dragonchang.domain.po.CompanyPriceRecord;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.UpwardTrend;
import com.dragonchang.mapper.CompanyPriceRecordMapper;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.mapper.UpwardTrendMapper;
import com.dragonchang.service.IUpwardTrendService;
import com.dragonchang.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-07-21 15:06
 **/
@Slf4j
@Service
public class UpwardTrendService implements IUpwardTrendService {
    @Autowired
    private CompanyStockMapper companyStockMapper;

    @Autowired
    private UpwardTrendMapper upwardTrendMapper;

    @Autowired
    private CompanyPriceRecordMapper companyPriceRecordMapper;

    @Override
    public void generateUpwardTrendListByToday(String today) {
        if (StringUtils.isBlank(today)) {
            return;
        }
        List<CompanyStock> companyStockList = companyStockMapper.selectList(new LambdaQueryWrapper<CompanyStock>());
        //遍历所有公司
        for (CompanyStock stock : companyStockList) {
            if(stock.getName().contains("ST")) {
                continue;
            }
            List<CompanyPriceRecord> priceRecords = companyPriceRecordMapper.selectListByCondition(stock.getId(), today, 90);
            if(priceRecords.size() < 90) {
                continue;
            }
            BigDecimal ten = calculateMA(10, priceRecords.subList(0, 10));
            BigDecimal twenty = calculateMA(20, priceRecords.subList(0, 20));
            BigDecimal thirty = calculateMA(30, priceRecords.subList(0, 30));
            BigDecimal liushi = calculateMA(60, priceRecords.subList(0, 60));
            BigDecimal jiushi = calculateMA(90, priceRecords.subList(0, 90));
            if (ten != null && twenty != null && thirty != null && liushi != null && jiushi != null) {
                if (ten.compareTo(twenty) > 0 && twenty.compareTo(thirty) > 0
                        && thirty.compareTo(liushi) >0 && liushi.compareTo(jiushi)>0) {
                    UpwardTrend upwardTrend = new UpwardTrend();
                    upwardTrend.setCompanyStockId(stock.getId());
                    upwardTrend.setReportTime(today);
                    upwardTrendMapper.insert(upwardTrend);
                }
            }
        }
    }

    /**
     * 算出公司公司股票在dayCount中的均价
     *
     * @param dayCount
     * @return
     */
    private BigDecimal calculateMA(Integer dayCount, List<CompanyPriceRecord> priceRecords) {
        if (CollectionUtils.isNotEmpty(priceRecords)) {
            if(priceRecords.size() < dayCount) {
                return null;
            }
            BigDecimal total = null;
            int count = 0;
            for (CompanyPriceRecord record : priceRecords) {
                if (record == null || record.getClosePrice() == null || !isNumeric(record.getClosePrice())) {
                    continue;
                }
                if (total != null) {
                    total = total.add(new BigDecimal(record.getClosePrice()));
                } else {
                    total = new BigDecimal(record.getClosePrice());
                }
                count ++;
            }
            if (total != null) {
                return total.divide(new BigDecimal(count), 4, RoundingMode.HALF_UP);
            }
        }
        return null;
    }

    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim())) {
            if (s.startsWith("-")) {
                s = s.substring(1, s.length());
            }
            return s.matches("^[0.0-9.0]+$");
        } else {
            return false;
        }
    }
}
