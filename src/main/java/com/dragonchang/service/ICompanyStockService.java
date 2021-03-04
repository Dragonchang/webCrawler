package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.po.CompanyStock;

/**
 * @author 63474
 */
public interface ICompanyStockService {

    /**
     * 同步公司股票列表信息
     */
    void syncStockListInfo();

    /**
     * 分页查询公司信息
     *
     * @param pageRequest
     * @return
     */
    IPage<CompanyStock> findPage(CompanyStockRequestDTO pageRequest);
}
