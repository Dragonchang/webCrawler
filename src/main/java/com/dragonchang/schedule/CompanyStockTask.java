package com.dragonchang.schedule;

import com.dragonchang.service.ICompanyStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

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
    /**
     * 每天下午3点执行
     */
    @Async
    @Scheduled(cron = "0 0 15 * * ? ")
    @Transactional
    public void tempMigrateWithMerchantTask() {
        companyStockService.syncStockListInfo();
    }
}
