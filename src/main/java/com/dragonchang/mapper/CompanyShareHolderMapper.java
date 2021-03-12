package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dragonchang.domain.dto.HolderCompanyListDTO;
import com.dragonchang.domain.po.CompanyShareHolder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 63474
 */
public interface CompanyShareHolderMapper extends BaseMapper<CompanyShareHolder> {

    List<HolderCompanyListDTO> getHolderListByName(@Param("name") String name);
}
