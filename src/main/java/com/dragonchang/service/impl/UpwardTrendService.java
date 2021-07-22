package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.UpwardTrendDTO;
import com.dragonchang.domain.dto.UpwardTrendPageRequestDTO;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
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
            if(priceRecords.size() < 60) {
                continue;
            }
            BigDecimal avgFive = calculateMA(5, priceRecords.subList(0, 5));
            BigDecimal avgTen = calculateMA(10, priceRecords.subList(0, 10));
            BigDecimal avgTwenty = calculateMA(20, priceRecords.subList(0, 20));
            BigDecimal avgThirty = calculateMA(30, priceRecords.subList(0, 30));
            BigDecimal avgSixty = calculateMA(60, priceRecords.subList(0, 60));
            BigDecimal avgNinety = null;
            if(priceRecords.size() >= 90) {
                avgNinety = calculateMA(90, priceRecords.subList(0, 90));
            }
            if (avgTen != null && avgTwenty != null && avgThirty != null && avgSixty != null) {
                if (avgTen.compareTo(avgTwenty) > 0 && avgTwenty.compareTo(avgThirty) > 0
                        && avgThirty.compareTo(avgSixty) >0) {
                    UpwardTrend upwardTrend = new UpwardTrend();
                    upwardTrend.setCompanyStockId(stock.getId());
                    if(avgFive != null) {
                        upwardTrend.setAvgFive(avgFive.toString());
                    }
                    if(avgTen != null) {
                        upwardTrend.setAvgTen(avgTen.toString());
                    }
                    if(avgTwenty != null) {
                        upwardTrend.setAvgTwenty(avgTwenty.toString());
                    }
                    if(avgThirty != null) {
                        upwardTrend.setAvgThirty(avgThirty.toString());
                    }
                    if(avgSixty != null) {
                        upwardTrend.setAvgSixty(avgSixty.toString());
                    }
                    if(avgNinety != null) {
                        upwardTrend.setAvgNinety(avgNinety.toString());
                    }
                    upwardTrend.setReportTime(today);
                    upwardTrendMapper.insert(upwardTrend);
                }
            }
        }
    }

    @Override
    public IPage<UpwardTrendDTO> findPage(UpwardTrendPageRequestDTO pageRequest) {
        Page page = new Page();
        page.setCurrent(pageRequest.getPage());
        page.setSize(pageRequest.getSize());
        return upwardTrendMapper.findPage(page, pageRequest);
    }

    @Override
    public ExcelData exportFlow(UpwardTrendPageRequestDTO request) {
        List<UpwardTrendDTO> list = upwardTrendMapper.findList(request);
        ExcelData data = new ExcelData();
        String time = DateUtil.localDateTimeFormat(LocalDateTime.now(),DateUtil.DEFAULT_DATE_TIME_FORMAT_SECOND);
        String fileName = "upwardTrend" + time;
        data.setSavePath("D:\\");
        data.setFileName(fileName);
        data.setSheetName("UpwardTrend");
        List<String> titles = new ArrayList();
        titles.add("股票代码");
        titles.add("公司名称");
        titles.add("股票最新股价");
        titles.add("五日");
        titles.add("十日");
        titles.add("二十日");
        titles.add("三十日");
        titles.add("六十日");
        titles.add("九十日");
        titles.add("一百日");
        titles.add("生成时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (int i = 0, length = list.size(); i < length; i++) {
            UpwardTrendDTO upwardTrendDTO = list.get(i);
            List<Object> row = new ArrayList();
            row.add(upwardTrendDTO.getStockCode());
            row.add(upwardTrendDTO.getName());
            row.add(upwardTrendDTO.getLastPrice());
            row.add(upwardTrendDTO.getAvgFive());
            row.add(upwardTrendDTO.getAvgTen());
            row.add(upwardTrendDTO.getAvgTwenty());
            row.add(upwardTrendDTO.getAvgThirty());
            row.add(upwardTrendDTO.getAvgSixty());
            row.add(upwardTrendDTO.getAvgNinety());
            row.add(upwardTrendDTO.getAvgHundtwenty());
            row.add(upwardTrendDTO.getReportTime());
            rows.add(row);
        }
        data.setRows(rows);
        return data;
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
