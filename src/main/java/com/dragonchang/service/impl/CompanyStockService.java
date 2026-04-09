package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.crawler.EastMoneyCrawler;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.eastmoney.StockInfoDto;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.TotalStockRecord;
import com.dragonchang.domain.po.CompanyPriceRecord;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.mapper.TotalStockRecordMapper;
import com.dragonchang.mapper.CompanyPriceRecordMapper;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
    private CompanyPriceRecordMapper companyPriceRecordMapper;

    @Autowired
    private CompanyShareHolderService companyShareHolderService;

    private static BigDecimal BillionUnits = new BigDecimal(100000000);
    @Override
    public void syncStockListInfo() {
        List<StockInfoDto> stockInfoDtoList = eastMoneyCrawler.getStockList();
        if (CollectionUtils.isNotEmpty(stockInfoDtoList)) {
            long totalStart = System.currentTimeMillis();
            int cpuThreads = Runtime.getRuntime().availableProcessors() * 2;
            int threadCount = Math.min(cpuThreads, stockInfoDtoList.size());
            int chunkSize = (stockInfoDtoList.size() + threadCount - 1) / threadCount;

            log.info("syncStockListInfo start, totalStock={}, cpuThreads={}, workerThreads={}, chunkSize={}",
                    stockInfoDtoList.size(), cpuThreads, threadCount, chunkSize);

            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            List<Future<StockSyncSummary>> futures = new ArrayList<>();
            for (int i = 0; i < stockInfoDtoList.size(); i += chunkSize) {
                int start = i;
                int end = Math.min(i + chunkSize, stockInfoDtoList.size());
                List<StockInfoDto> stockChunk = new ArrayList<>(stockInfoDtoList.subList(start, end));
                int chunkNo = (i / chunkSize) + 1;
                futures.add(executorService.submit(() -> processStockChunk(stockChunk, chunkNo, start, end)));
            }

            BigDecimal totalPrice = BigDecimal.ZERO;
            BigDecimal totalCapitalization = BigDecimal.ZERO;
            BigDecimal totalCirculation = BigDecimal.ZERO;
            try {
                for (Future<StockSyncSummary> future : futures) {
                    StockSyncSummary summary = future.get();
                    totalPrice = totalPrice.add(summary.getTotalPrice());
                    totalCapitalization = totalCapitalization.add(summary.getTotalCapitalization());
                    totalCirculation = totalCirculation.add(summary.getTotalCirculation());
                }
            } catch (Exception e) {
                log.error("syncStockListInfo parallel execute error", e);
                throw new RuntimeException("syncStockListInfo parallel execute error", e);
            } finally {
                executorService.shutdown();
                try {
                    if (!executorService.awaitTermination(5, TimeUnit.MINUTES)) {
                        executorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executorService.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }

            TotalStockRecord totalStockRecord = new TotalStockRecord();
            totalStockRecord.setAveragePrice(totalPrice.divide(new BigDecimal(stockInfoDtoList.size()), 6, RoundingMode.HALF_UP));
            totalStockRecord.setTotalCapitalization(totalCapitalization);
            totalStockRecord.setLastCirculation(totalCirculation);
            totalStockRecordMapper.insert(totalStockRecord);

            long totalCost = System.currentTimeMillis() - totalStart;
            log.info("syncStockListInfo finish, totalStock={}, workerThreads={}, chunkSize={}, totalCost={}ms",
                    stockInfoDtoList.size(), threadCount, chunkSize, totalCost);
        } else {
            log.warn("get stock list failed!");
        }
    }

    private StockSyncSummary processStockChunk(List<StockInfoDto> stockChunk, int chunkNo, int start, int end) {
        long chunkStart = System.currentTimeMillis();
        log.info("stock chunk {} start, range=[{}, {}), size={}", chunkNo, start, end, stockChunk.size());
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalCapitalization = BigDecimal.ZERO;
        BigDecimal totalCirculation = BigDecimal.ZERO;
        for (StockInfoDto stockInfoDto : stockChunk) {
            CompanyStock companyStock = mapper.selectOne(new LambdaQueryWrapper<CompanyStock>()
                    .eq(CompanyStock::getStockCode, stockInfoDto.getF12()));
            String marketTime = stockInfoDto.getF26();
            log.info(marketTime);
            if (companyStock == null) {
                companyStock = new CompanyStock();
                companyStock.setStockCode(stockInfoDto.getF12());
            }

            companyStock.setName(stockInfoDto.getF14());
            if (StringUtils.isNotEmpty(marketTime)) {
                companyStock.setMarketTime(DateUtil.strToLocalDateTime(stockInfoDto.getF26()));
            }
            if (StringUtils.isNotEmpty(stockInfoDto.getF3())) {
                companyStock.setDtzf(stockInfoDto.getF3());
            }
            if (StringUtils.isNotEmpty(stockInfoDto.getF5())) {
                companyStock.setDtcjl(stockInfoDto.getF5());
            }
            if (StringUtils.isNotEmpty(stockInfoDto.getF6()) && StringUtil.isNumeric2(stockInfoDto.getF6())) {
                companyStock.setDtcjje(new BigDecimal(stockInfoDto.getF6()));
            }
            if (StringUtils.isNotEmpty(stockInfoDto.getF8())) {
                companyStock.setDthsl(stockInfoDto.getF8());
            }
            if (StringUtils.isNotEmpty(stockInfoDto.getF10())) {
                companyStock.setLb(stockInfoDto.getF10());
            }
            if (StringUtils.isNotEmpty(stockInfoDto.getF115())) {
                companyStock.setSyl(stockInfoDto.getF115());
            }
            if (!StringUtils.isEmpty(stockInfoDto.getF2()) && !stockInfoDto.getF2().equals("-")) {
                BigDecimal price = new BigDecimal(stockInfoDto.getF2());
                totalPrice = totalPrice.add(price);
                companyStock.setLastPrice(price);
            } else if (!StringUtils.isEmpty(stockInfoDto.getF18()) && !stockInfoDto.getF18().equals("-")) {
                BigDecimal price = new BigDecimal(stockInfoDto.getF18());
                companyStock.setLastPrice(price);
            }
//                if (!StringUtils.isEmpty(stockInfoDto.getF55()) && !stockInfoDto.getF55().equals("-") && StringUtil.isNumeric2(stockInfoDto.getF55())) {
//                    companyStock.setLastIncome(new BigDecimal(stockInfoDto.getF55()));
//                }
            if (!StringUtils.isEmpty(stockInfoDto.getF20()) && !stockInfoDto.getF20().equals("-") && StringUtil.isNumeric2(stockInfoDto.getF20())) {
                BigDecimal stockTotalCapitalization = new BigDecimal(stockInfoDto.getF20()).divide(BillionUnits, 4, RoundingMode.HALF_UP);
                totalCapitalization = totalCapitalization.add(stockTotalCapitalization);
                companyStock.setTotalCapitalization(stockTotalCapitalization);
            }
            if (!StringUtils.isEmpty(stockInfoDto.getF21()) && !stockInfoDto.getF21().equals("-") && StringUtil.isNumeric2(stockInfoDto.getF21())) {
                BigDecimal circulation = new BigDecimal(stockInfoDto.getF21()).divide(BillionUnits, 4, RoundingMode.HALF_UP);
                totalCirculation = totalCirculation.add(circulation);
                companyStock.setLastCirculation(circulation);
            }
            companyStock.setUpdatedTime(LocalDateTime.now());
            if (companyStock.getId() == null) {
                mapper.insert(companyStock);
            } else {
                mapper.updateById(companyStock);
            }
            syncTodayPriceRecord(companyStock, stockInfoDto);
        }
        long chunkCost = System.currentTimeMillis() - chunkStart;
        log.info("stock chunk {} finish, size={}, cost={}ms", chunkNo, stockChunk.size(), chunkCost);
        return new StockSyncSummary(totalPrice, totalCapitalization, totalCirculation);
    }

    private static class StockSyncSummary {
        private final BigDecimal totalPrice;
        private final BigDecimal totalCapitalization;
        private final BigDecimal totalCirculation;

        private StockSyncSummary(BigDecimal totalPrice, BigDecimal totalCapitalization, BigDecimal totalCirculation) {
            this.totalPrice = totalPrice;
            this.totalCapitalization = totalCapitalization;
            this.totalCirculation = totalCirculation;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public BigDecimal getTotalCapitalization() {
            return totalCapitalization;
        }

        public BigDecimal getTotalCirculation() {
            return totalCirculation;
        }
    }

    private void syncTodayPriceRecord(CompanyStock companyStock, StockInfoDto stockInfoDto) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = sdf.format(d);
        CompanyPriceRecord record = companyPriceRecordMapper.selectOne(new LambdaQueryWrapper<CompanyPriceRecord>()
                .eq(CompanyPriceRecord::getCompanyStockId, companyStock.getId())
                .eq(CompanyPriceRecord::getReportTime, currentTime));
        if (record == null) {
            record = new CompanyPriceRecord();
            record.setCompanyStockId(companyStock.getId());
            record.setReportTime(currentTime);
        }
        record.setOpenPrice(stockInfoDto.getF17());
        record.setClosePrice(stockInfoDto.getF2());
        record.setHighestPrice(stockInfoDto.getF15());
        record.setLowestPrice(stockInfoDto.getF16());
        record.setDtzf(companyStock.getDtzf());
        record.setDtcjl(companyStock.getDtcjl());
        record.setDtcjje(companyStock.getDtcjje());
        record.setDthsl(companyStock.getDthsl());
        record.setLb(companyStock.getLb());
        record.setSyl(companyStock.getSyl());
        if (record.getId() == null) {
            companyPriceRecordMapper.insert(record);
        } else {
            companyPriceRecordMapper.updateById(record);
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
