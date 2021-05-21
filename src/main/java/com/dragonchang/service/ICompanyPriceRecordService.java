package com.dragonchang.service;

import java.util.List;

public interface ICompanyPriceRecordService {
    /**
     * 同步所有公司的历史股价
     */
    void syncCompanyPrice(Integer companyStockId);

    /**
     * 同步所有公司的当天股价
     */
    void syncCompanyTodayPrice();

    /**
     * 获取公司的K line集合
     * @param companyStockId
     * @return
     */
    List<List<String>> getPriceRecordByCompany(Integer companyStockId);

    /**
     * 同步所有公司的财务信息
     */
    void syncAllCompanyFinance(Integer companyStockId);

}
