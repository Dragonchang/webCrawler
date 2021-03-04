package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.dto.tyc.ShareStructureRequestDto;
import com.dragonchang.domain.po.Company;
import com.dragonchang.domain.po.ShareStructure;

import java.util.List;

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

    /**
     * 为指定公司同步股权穿透信息
     * @param companyId
     */
    void syncShareInfoWithCompanyId(Long companyId);

    /**
     * 通过ID获取公司信息
     * @param companyId
     * @return
     */
    Company getCompanyById(Long companyId);

    /**
     * 获取所有关注公司机构或者个人的list
     * @return
     */
    List<Company> getAllFocusCompanyList();
}
