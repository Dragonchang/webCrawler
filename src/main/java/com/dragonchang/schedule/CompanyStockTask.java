package com.dragonchang.schedule;

import com.dragonchang.service.IBKInfoService;
import com.dragonchang.service.ICompanyPriceRecordService;
import com.dragonchang.service.ICompanyStockService;
import com.dragonchang.service.IUpwardTrendService;
import com.dragonchang.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-04 08:47
 **/
@Configuration
@EnableScheduling
@EnableAsync
@Slf4j
public class CompanyStockTask {

    @Autowired
    ICompanyStockService companyStockService;

    @Autowired
    IUpwardTrendService upwardTrendService;

    @Autowired
    IBKInfoService bKInfoService;

    @Autowired
    ICompanyPriceRecordService companyPriceRecordService;
    /**
     * 每天下午3点执行
     */
    @Async
    @Scheduled(cron = "0 0 15 ? * MON-FRI")
    public void tempMigrateWithMerchantTask() {
        companyStockService.syncStockListInfo();
        companyStockService.syncAllStockShareHolder();

        bKInfoService.updateBKInfo();
        bKInfoService.updateConceptInfo();
    }

    /**
     * 每天下午11点执行
     */
    @Async
    @Scheduled(cron = "0 0 23 ? * MON-FRI")
    public void syncFinance() {
        companyPriceRecordService.syncAllCompanyFinance(null);
    }

    /**
     * 每天下午6点执行
     */
    @Async
    @Scheduled(cron = "0 0 18 ? * MON-FRI")
    public void createUpwardTrendRecord() {
        String today = DateUtil.formatDate(new Date());
        upwardTrendService.generateUpwardTrendListByToday(today);
        upwardTrendService.generateNewUpwardTrendListByToday(today);
    }
}
