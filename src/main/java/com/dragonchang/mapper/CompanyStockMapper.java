package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.po.CompanyStock;
import org.apache.ibatis.annotations.Param;

/**
 * @author 63474
 */
public interface CompanyStockMapper extends BaseMapper<CompanyStock> {
    /**
     *
     * @param page
     * @param request
     * @return
     */
    IPage<CompanyStock> findPage(Page page, @Param("request") CompanyStockRequestDTO request);
}
