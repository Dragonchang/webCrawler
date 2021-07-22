package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.UpwardTrendDTO;
import com.dragonchang.domain.dto.UpwardTrendPageRequestDTO;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.UpwardTrend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UpwardTrendMapper  extends BaseMapper<UpwardTrend> {
    /**
     *
     * @param page
     * @param request
     * @return
     */
    IPage<UpwardTrendDTO> findPage(Page page, @Param("request") UpwardTrendPageRequestDTO request);

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<UpwardTrendDTO> findList(@Param("request") UpwardTrendPageRequestDTO request);
}
