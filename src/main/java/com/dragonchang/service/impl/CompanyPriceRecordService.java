package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragonchang.crawler.EastMoneyCrawler;
import com.dragonchang.domain.dto.eastmoney.KlineDetailDTO;
import com.dragonchang.domain.dto.eastmoney.TodayPriceDTO;
import com.dragonchang.domain.po.CompanyPriceRecord;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.mapper.CompanyPriceRecordMapper;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.service.ICompanyPriceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public void syncCompanyTodayPrice() {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = sdf.format(d);
        System.out.println("格式化后的日期：" + currentTime);
        List<CompanyStock> companyStockList = companyStockMapper.selectList(new LambdaQueryWrapper<CompanyStock>());
        for(CompanyStock stock : companyStockList) {
            TodayPriceDTO priceDTO = eastMoneyCrawler.getTodayPrice(stock.getStockCode());
            CompanyPriceRecord record = new CompanyPriceRecord();
            record.setOpenPrice(priceDTO.getF46());
            record.setClosePrice(priceDTO.getF43());
            record.setHighestPrice(priceDTO.getF44());
            record.setLowestPrice(priceDTO.getF45());
            record.setCompanyStockId(stock.getId());
            record.setReportTime(currentTime);
            CompanyPriceRecord query = companyPriceRecordMapper.selectOne(new LambdaQueryWrapper<CompanyPriceRecord>()
                    .eq(CompanyPriceRecord::getCompanyStockId, stock.getId())
                    .eq(CompanyPriceRecord::getReportTime, currentTime));
            if(query != null) {
                continue;
            }
            companyPriceRecordMapper.insert(record);
        }
    }

    @Override
    public List<List<String>> getPriceRecordByCompany(Integer companyStockId) {
        List<List<String>> ret = new ArrayList<>();
        List<CompanyPriceRecord> priceRecords = companyPriceRecordMapper.selectList(new LambdaQueryWrapper<CompanyPriceRecord>()
                            .eq(CompanyPriceRecord::getCompanyStockId, companyStockId));
        for(CompanyPriceRecord record : priceRecords) {
            List<String> strRecords = new ArrayList<>();
            strRecords.add(record.getReportTime());
            strRecords.add(record.getOpenPrice());
            strRecords.add(record.getClosePrice());
            strRecords.add(record.getLowestPrice());
            strRecords.add(record.getHighestPrice());
            ret.add(strRecords);
        }

        return ret;
    }
}
