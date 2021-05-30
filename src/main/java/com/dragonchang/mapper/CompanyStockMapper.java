package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.FinanceAnalysisRequestDTO;
import com.dragonchang.domain.dto.FinanceAnalysisResponseDTO;
import com.dragonchang.domain.po.CompanyStock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<CompanyStock> findList(@Param("request") CompanyStockRequestDTO request);
}
