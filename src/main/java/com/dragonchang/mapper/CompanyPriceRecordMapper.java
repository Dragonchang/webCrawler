package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dragonchang.domain.po.CompanyPriceRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyPriceRecordMapper extends BaseMapper<CompanyPriceRecord> {

    List<CompanyPriceRecord> selectListByCondition(@Param("companyStockId") Integer companyStockId,
                                                   @Param("today") String today,
                                                   @Param("dayCount") Integer dayCount);
}
