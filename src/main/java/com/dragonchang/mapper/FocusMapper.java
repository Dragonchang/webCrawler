package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.FocusDTO;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.po.Focus;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author 63474
 */
public interface FocusMapper extends BaseMapper<Focus> {

    /**
     *
     * @param page
     * @param request
     * @return
     */
    IPage<FocusDTO> findPage(Page page, @Param("request") CompanyRequestDTO request);
}
