package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.crawler.EastMoneyCrawler;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.eastmoney.StockDetailDto;
import com.dragonchang.domain.dto.eastmoney.StockInfoDto;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.TotalStockRecord;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.mapper.TotalStockRecordMapper;
import com.dragonchang.service.ICompanyStockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-23 17:24
 **/
@Slf4j
@Service
public class CompanyStockService implements ICompanyStockService {

    @Autowired
    EastMoneyCrawler eastMoneyCrawler;

    @Autowired
    CompanyStockMapper mapper;

    @Autowired
    TotalStockRecordMapper totalStockRecordMapper;

    private static BigDecimal BillionUnits = new BigDecimal(100000000);
    @Override
    public void syncStockListInfo() {
        List<StockInfoDto> stockInfoDtoList = eastMoneyCrawler.getStockList();
        if (CollectionUtils.isNotEmpty(stockInfoDtoList)) {
            BigDecimal total_price = new BigDecimal(0);
            BigDecimal total_capitalization = new BigDecimal(0);
            BigDecimal total_circulation = new BigDecimal(0);
            for (StockInfoDto stockInfoDto : stockInfoDtoList) {
                //获取详细信息
                StockDetailDto detailDto = eastMoneyCrawler.getStockInfoByStockCode(stockInfoDto.getF12());
                CompanyStock companyStock = mapper.selectOne(new LambdaQueryWrapper<CompanyStock>()
                        .eq(CompanyStock::getStockCode, stockInfoDto.getF12()));

                if (companyStock != null) {
                    companyStock.setName(stockInfoDto.getF14());
                    if (!StringUtils.isEmpty(stockInfoDto.getF2()) && !stockInfoDto.getF2().equals("-")) {
                        BigDecimal price = new BigDecimal(stockInfoDto.getF2());
                        total_price = total_price.add(price);
                        companyStock.setLastPrice(price);
                    } else {
                        if(!StringUtils.isEmpty(stockInfoDto.getF18()) && !stockInfoDto.getF18().equals("-")) {
                            BigDecimal price = new BigDecimal(stockInfoDto.getF18());
                            total_price = total_price.add(price);
                            companyStock.setLastPrice(new BigDecimal(stockInfoDto.getF18()));
                        }
                    }
                    if(detailDto != null) {
                        if (!StringUtils.isEmpty(detailDto.getF55()) && !detailDto.getF55().equals("-")) {
                            companyStock.setLastIncome(new BigDecimal(detailDto.getF55()));
                        }
                        if (!StringUtils.isEmpty(detailDto.getF116()) && !detailDto.getF116().equals("-")) {
                            BigDecimal totalCapitalization = new BigDecimal(detailDto.getF116()).divide(BillionUnits, 4, RoundingMode.HALF_UP);
                            total_capitalization = total_capitalization.add(totalCapitalization);
                            companyStock.setTotalCapitalization(totalCapitalization);
                        }
                        if (!StringUtils.isEmpty(detailDto.getF117()) && !detailDto.getF117().equals("-")) {
                            BigDecimal circulation = new BigDecimal(detailDto.getF117()).divide(BillionUnits, 4, RoundingMode.HALF_UP);
                            total_circulation = total_circulation.add(circulation);
                            companyStock.setLastCirculation(circulation);
                        }
                    }
                    companyStock.setUpdatedTime(LocalDateTime.now());
                    mapper.updateById(companyStock);
                } else {
                    companyStock = new CompanyStock();
                    companyStock.setStockCode(stockInfoDto.getF12());
                    companyStock.setName(stockInfoDto.getF14());
                    if (!StringUtils.isEmpty(stockInfoDto.getF2()) && !stockInfoDto.getF2().equals("-")) {
                        BigDecimal price = new BigDecimal(stockInfoDto.getF2());
                        total_price = total_price.add(price);
                        companyStock.setLastPrice(new BigDecimal(stockInfoDto.getF2()));
                    } else {
                        if(!StringUtils.isEmpty(stockInfoDto.getF18()) && !stockInfoDto.getF18().equals("-")) {
                            BigDecimal price = new BigDecimal(stockInfoDto.getF18());
                            total_price = total_price.add(price);
                            companyStock.setLastPrice(new BigDecimal(stockInfoDto.getF18()));
                        }
                    }
                    if(detailDto != null) {
                        if (!StringUtils.isEmpty(detailDto.getF55()) && !detailDto.getF55().equals("-")) {
                            companyStock.setLastIncome(new BigDecimal(detailDto.getF55()));
                        }
                        if (!StringUtils.isEmpty(detailDto.getF116()) && !detailDto.getF116().equals("-")) {
                            BigDecimal totalCapitalization = new BigDecimal(detailDto.getF116()).divide(BillionUnits, 4, RoundingMode.HALF_UP);
                            total_capitalization = total_capitalization.add(totalCapitalization);
                            companyStock.setTotalCapitalization(totalCapitalization);
                        }
                        if (!StringUtils.isEmpty(detailDto.getF117()) && !detailDto.getF117().equals("-")) {
                            BigDecimal circulation = new BigDecimal(detailDto.getF117()).divide(BillionUnits, 4, RoundingMode.HALF_UP);
                            total_circulation = total_circulation.add(circulation);
                            companyStock.setLastCirculation(circulation);
                        }
                    }
                    mapper.insert(companyStock);
                }
            }
            TotalStockRecord totalStockRecord = new TotalStockRecord();
            totalStockRecord.setAveragePrice(total_price.divide(new BigDecimal(stockInfoDtoList.size()), 6, RoundingMode.HALF_UP));
            totalStockRecord.setTotalCapitalization(total_capitalization);
            totalStockRecord.setLastCirculation(total_circulation);
            totalStockRecordMapper.insert(totalStockRecord);
        } else {
            log.warn("get stock list failed!");
        }
    }

    @Override
    public IPage<CompanyStock> findPage(CompanyStockRequestDTO pageRequest) {
        Page page = new Page();
        page.setCurrent(pageRequest.getPage());
        page.setSize(pageRequest.getSize());
        return mapper.findPage(page, pageRequest);
    }
}
