package com.dragonchang.schedule;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dragonchang.domain.po.Company;
import com.dragonchang.service.ICompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-18 11:40
 **/
@Configuration
@EnableScheduling
@EnableAsync
@Slf4j
public class CompanyShareTask {

    @Autowired
    ICompanyService companyService;
    /**
     * 每天晚上1点执行
     */
    @Async
    @Scheduled(cron = "0 0 1 * * ? ")
    @Transactional
    public void tempMigrateWithMerchantTask() {
        List<Company> companyList = companyService.getAllFocusCompanyList();
        if(CollectionUtils.isNotEmpty(companyList)) {
            for (Company company : companyList) {
                companyService.syncShareInfoWithCompanyId(company.getId());
            }
        }
    }

}
