package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragonchang.crawler.EastMoneyCrawler;
import com.dragonchang.domain.dto.eastmoney.KlineDetailDTO;
import com.dragonchang.domain.po.CompanyPriceRecord;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.mapper.CompanyPriceRecordMapper;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.service.ICompanyPriceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-17 19:21
 **/
@Slf4j
@Service
public class CompanyPriceRecordService implements ICompanyPriceRecordService {
    @Autowired
    private CompanyStockMapper companyStockMapper;
    @Autowired
    private CompanyPriceRecordMapper companyPriceRecordMapper;
    @Autowired
    private EastMoneyCrawler eastMoneyCrawler;

    @Override
    public void syncCompanyPrice() {
        List<CompanyStock> companyStockList = companyStockMapper.selectList(new LambdaQueryWrapper<CompanyStock>());
        for(CompanyStock stock : companyStockList) {
            KlineDetailDTO dto = eastMoneyCrawler.getKlineData(stock.getStockCode());
            if(dto != null) {
                for (String kline : dto.getKlines()) {
                    if(StringUtils.isNotBlank(kline)) {
                        String [] line = kline.split(",");
                        if(line != null && line.length >1) {
                            CompanyPriceRecord record = new CompanyPriceRecord();
                            record.setCompanyStockId(stock.getId());
                            record.setOpenPrice(line[1]);
                            record.setClosePrice(line[2]);
                            record.setHighestPrice(line[3]);
                            record.setLowestPrice(line[4]);
                            record.setReportTime(line[0]);
                            CompanyPriceRecord query = companyPriceRecordMapper.selectOne(new LambdaQueryWrapper<CompanyPriceRecord>()
                            .eq(CompanyPriceRecord::getCompanyStockId, stock.getId())
                            .eq(CompanyPriceRecord::getReportTime, line[0]));
                            if(query != null) {
                                continue;
                            }
                            companyPriceRecordMapper.insert(record);
                        }
                    }
                }
            }
        }
    }
}
