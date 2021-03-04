package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.crawler.EastMoneyCrawler;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.eastmoney.StockInfoDto;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.service.ICompanyStockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void syncStockListInfo() {
        List<StockInfoDto> stockInfoDtoList = eastMoneyCrawler.getStockList();
        if (CollectionUtils.isNotEmpty(stockInfoDtoList)) {
            for (StockInfoDto stockInfoDto : stockInfoDtoList) {
                CompanyStock companyStock = mapper.selectOne(new LambdaQueryWrapper<CompanyStock>()
                        .eq(CompanyStock::getStockCode, stockInfoDto.getF12()));
                if (companyStock != null) {
                    companyStock.setName(stockInfoDto.getF14());
                    if (!StringUtils.isEmpty(stockInfoDto.getF2()) && !stockInfoDto.getF2().equals("-")) {
                        companyStock.setLastPrice(stockInfoDto.getF2());
                    } else {
                        companyStock.setLastPrice(stockInfoDto.getF18());
                    }
                    companyStock.setUpdatedTime(LocalDateTime.now());
                    mapper.updateById(companyStock);
                } else {
                    companyStock = new CompanyStock();
                    companyStock.setStockCode(stockInfoDto.getF12());
                    companyStock.setName(stockInfoDto.getF14());
                    if (!StringUtils.isEmpty(stockInfoDto.getF2()) && !stockInfoDto.getF2().equals("-")) {
                        companyStock.setLastPrice(stockInfoDto.getF2());
                    } else {
                        companyStock.setLastPrice(stockInfoDto.getF18());
                    }
                    mapper.insert(companyStock);
                }
            }
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
