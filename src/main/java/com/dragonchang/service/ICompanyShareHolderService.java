package com.dragonchang.service;

import com.dragonchang.domain.po.CompanyShareHolder;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.ShareHolderDetail;

import java.util.List;

/**
 * @author 63474
 */
public interface ICompanyShareHolderService {

    /**
     * 同步公司股票列表信息
     */
    void syncStockHolderByCode(CompanyStock companyStock);

    /**
     * 通过stockid获取股东发布列表
     * @param stockId
     * @return
     */
    List<CompanyShareHolder> getHodlderListByStockId(Long stockId);

    /**
     * 获取持股股东详情
     * @param holderId
     * @return
     */
    List<ShareHolderDetail> getHodlderDetailListByStockId(Long holderId);
}
