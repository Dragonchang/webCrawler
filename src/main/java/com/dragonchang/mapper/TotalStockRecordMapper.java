package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dragonchang.domain.po.TotalStockRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TotalStockRecordMapper extends BaseMapper<TotalStockRecord> {

    List<TotalStockRecord> seletListByTime(@Param("startDate")Date startDate, @Param("endDate")Date endDate);
}
