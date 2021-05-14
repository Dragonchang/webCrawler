package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.HolderCompanyListDTO;
import com.dragonchang.domain.dto.HolderDetailRequestDTO;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.po.CompanyShareHolder;
import com.dragonchang.domain.po.Focus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 63474
 */
public interface CompanyShareHolderMapper extends BaseMapper<CompanyShareHolder> {

    List<HolderCompanyListDTO> getHolderListByName(@Param("name") String name);

    /**
     *
     * @param page
     * @param request
     * @return
     */
    IPage<HolderCompanyListDTO> findPage(Page page, @Param("request") HolderDetailRequestDTO request);

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<HolderCompanyListDTO> findList(@Param("request") HolderDetailRequestDTO request);
}
