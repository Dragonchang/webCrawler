package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.ShareStructureDTO;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.dto.tyc.ShareStructureRequestDto;
import com.dragonchang.domain.po.Company;
import com.dragonchang.domain.po.ShareStructure;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 63474
 */
public interface ShareStructureMapper extends BaseMapper<ShareStructure> {

    List<ShareStructureDTO> findList(@Param("request") ShareStructureRequestDto request);
}
