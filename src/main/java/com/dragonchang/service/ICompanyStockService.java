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
     * 同步所有公司的流通股东
     */
    void syncAllStockShareHolder();

    /**
     * 分页查询公司信息
     *
     * @param pageRequest
     * @return
     */
    IPage<CompanyStock> findPage(CompanyStockRequestDTO pageRequest);

    /**
     * 通过id获取stock信息
     * @param id
     * @return
     */
    CompanyStock getStockById(Integer id);
}
