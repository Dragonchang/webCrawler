package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.crawler.EastMoneyCrawler;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.FinanceAnalysisResponseDTO;
import com.dragonchang.domain.dto.eastmoney.StockDetailDto;
import com.dragonchang.domain.dto.eastmoney.StockInfoDto;
import com.dragonchang.domain.enums.FinanceReportTypeEnum;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.TotalStockRecord;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.mapper.TotalStockRecordMapper;
import com.dragonchang.service.ICompanyStockService;
import com.dragonchang.util.DateUtil;
import com.dragonchang.util.ExcelUtil;
import com.dragonchang.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private EastMoneyCrawler eastMoneyCrawler;

    @Autowired
    private CompanyStockMapper mapper;

    @Autowired
    private TotalStockRecordMapper totalStockRecordMapper;

    @Autowired
    private CompanyShareHolderService companyShareHolderService;

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
                String marketTime = stockInfoDto.getF26();
                log.info(marketTime);
                if (companyStock != null) {
                    companyStock.setName(stockInfoDto.getF14());
                    if(StringUtils.isNotEmpty(marketTime)) {
                        companyStock.setMarketTime(DateUtil.strToLocalDateTime(stockInfoDto.getF26()));
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF3())) {
                        companyStock.setDtzf(stockInfoDto.getF3());
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF5())) {
                        companyStock.setDtcjl(stockInfoDto.getF5());
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF6()) && StringUtil.isNumeric2(stockInfoDto.getF6())) {
                        companyStock.setDtcjje(new BigDecimal(stockInfoDto.getF6()));
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF8())) {
                        companyStock.setDthsl(stockInfoDto.getF8());
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF10())) {
                        companyStock.setLb(stockInfoDto.getF10());
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF115())) {
                        companyStock.setSyl(stockInfoDto.getF115());
                    }
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
                    //同步股东信息
                    //companyShareHolderService.syncStockHolderByCode(companyStock);
                } else {
                    companyStock = new CompanyStock();
                    companyStock.setStockCode(stockInfoDto.getF12());
                    companyStock.setName(stockInfoDto.getF14());
                    if(StringUtils.isNotEmpty(marketTime)) {
                        companyStock.setMarketTime(DateUtil.strToLocalDateTime(stockInfoDto.getF26()));
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF3())) {
                        companyStock.setDtzf(stockInfoDto.getF3());
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF5())) {
                        companyStock.setDtcjl(stockInfoDto.getF5());
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF6()) && StringUtil.isNumeric2(stockInfoDto.getF6())) {
                        companyStock.setDtcjje(new BigDecimal(stockInfoDto.getF6()));
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF8())) {
                        companyStock.setDthsl(stockInfoDto.getF8());
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF10())) {
                        companyStock.setLb(stockInfoDto.getF10());
                    }
                    if(StringUtils.isNotEmpty(stockInfoDto.getF115())) {
                        companyStock.setSyl(stockInfoDto.getF115());
                    }
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
                    //同步股东信息
                    //companyShareHolderService.syncStockHolderByCode(companyStock);
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
    public void syncAllStockShareHolder() {
        List<CompanyStock> companyStockList = mapper.selectList(new LambdaQueryWrapper<CompanyStock>());
        for(CompanyStock stock : companyStockList) {
            companyShareHolderService.syncStockHolderByCode(stock);
        }
    }

    @Override
    public IPage<CompanyStock> findPage(CompanyStockRequestDTO pageRequest) {
        Page page = new Page();
        page.setCurrent(pageRequest.getPage());
        page.setSize(pageRequest.getSize());
        return mapper.findPage(page, pageRequest);
    }

    @Override
    public CompanyStock getStockById(Integer id) {
        CompanyStock companyStock = mapper.selectOne(new LambdaQueryWrapper<CompanyStock>()
                .eq(CompanyStock::getId, id));
        return companyStock;
    }

    @Override
    public ExcelData exportFlow(CompanyStockRequestDTO request) {
        List<CompanyStock> list = mapper.findList(request);
        ExcelData data = new ExcelData();
        String time = DateUtil.localDateTimeFormat(LocalDateTime.now(),DateUtil.DEFAULT_DATE_TIME_FORMAT_SECOND);
        String fileName = "company" + time;
        data.setSavePath("D:\\");
        data.setFileName(fileName);
        data.setSheetName("company");
        List<String> titles = new ArrayList();
        titles.add("股票代码");
        titles.add("公司名称");
        titles.add("股票最新股价");
        titles.add("当天涨幅(%)");
        titles.add("当天成交量（手）");
        titles.add("当天成交金额（亿）");
        titles.add("当天换手率(%)");
        titles.add("量比");
        titles.add("市盈率(%)");
        titles.add("股票最新总市值(亿)");
        titles.add("股票最新流通市值(亿)");
        titles.add("股票最新收益");
        titles.add("上市时间");
        titles.add("更新时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (int i = 0, length = list.size(); i < length; i++) {
            CompanyStock companyStock = list.get(i);
            List<Object> row = new ArrayList();
            row.add(companyStock.getStockCode());
            row.add(companyStock.getName());
            row.add(companyStock.getLastPrice());
            row.add(companyStock.getDtzf());
            row.add(companyStock.getDtcjl());
            row.add(ExcelUtil.convertToBillion(companyStock.getDtcjje()));
            row.add(companyStock.getDthsl());
            row.add(companyStock.getLb());
            row.add(companyStock.getSyl());
            row.add(companyStock.getTotalCapitalization());
            row.add(companyStock.getLastCirculation());
            row.add(companyStock.getLastIncome());
            row.add(companyStock.getMarketTime());
            row.add(companyStock.getUpdatedTime());
            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }
}
