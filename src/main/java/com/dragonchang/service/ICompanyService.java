package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.po.Company;

/**
 * @author 63474
 */
public interface ICompanyService {

    /**
     * 分页查询公司信息
     * @param pageRequest
     * @return
     */
    IPage<Company> findPage(CompanyRequestDTO pageRequest);
}
