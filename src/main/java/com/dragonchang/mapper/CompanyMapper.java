package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.po.Company;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author 63474
 */
public interface CompanyMapper extends BaseMapper<Company> {

    /**
     *
     * @param page
     * @param request
     * @return
     */
    IPage<Company> findPage(Page page, @Param("request") CompanyRequestDTO request);
}
