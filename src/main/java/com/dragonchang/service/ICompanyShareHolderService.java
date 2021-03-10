package com.dragonchang.service;

import com.dragonchang.domain.po.CompanyStock;

/**
 * @author 63474
 */
public interface ICompanyShareHolderService {

    /**
     * 同步公司股票列表信息
     */
    void syncStockHolderByCode(CompanyStock companyStock);
}
