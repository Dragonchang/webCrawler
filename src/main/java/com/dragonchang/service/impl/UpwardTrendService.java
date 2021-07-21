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
        if(StringUtils.isBlank(today)) {
            return;
        }
        List<CompanyStock> companyStockList = companyStockMapper.selectList(new LambdaQueryWrapper<CompanyStock>());
        //遍历所有公司
        for (CompanyStock stock : companyStockList) {
            BigDecimal ten = calculateMA(today, stock, 10);
            BigDecimal twenty  = calculateMA(today, stock, 20);
            BigDecimal thirty  = calculateMA(today, stock, 30);
            if(ten!= null &&  twenty != null && thirty!= null) {
                if(ten.compareTo(twenty) >=0 && twenty.compareTo(thirty)>=0) {
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
     * @param stock
     * @param dayCount
     * @return
     */
    private BigDecimal calculateMA(String today, CompanyStock stock ,Integer dayCount) {
        if(StringUtils.isBlank(today)) {
            return null;
        }
        Date dateToday = DateUtil.parseDate(today);
        Date backDay = DateUtil.getDateBefore(dateToday, dayCount);
        String back = DateUtil.formatDate(backDay);
        List<CompanyPriceRecord> priceRecords = companyPriceRecordMapper.selectList(new LambdaQueryWrapper<CompanyPriceRecord>()
                .eq(CompanyPriceRecord::getCompanyStockId, stock.getId())
                .le(CompanyPriceRecord::getReportTime, today)
                .ge(CompanyPriceRecord::getReportTime, back)
        );
        if(CollectionUtils.isNotEmpty(priceRecords)) {
            BigDecimal total = null;
            for (CompanyPriceRecord record : priceRecords) {
                if(record == null || record.getClosePrice() == null) {
                    return null;
                }
                if(total != null) {
                    total = total.add(new BigDecimal(record.getClosePrice()));
                } else {
                    total = new BigDecimal(record.getClosePrice());
                }
            }
            if(total != null) {
                return total.divide(new BigDecimal(dayCount), 4, RoundingMode.HALF_UP);
            }
        }
        return null;
    }
}
