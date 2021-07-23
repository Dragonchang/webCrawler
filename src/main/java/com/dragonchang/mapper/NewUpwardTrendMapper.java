package com.dragonchang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.NewUpwardTrendDTO;
import com.dragonchang.domain.dto.UpwardTrendDTO;
import com.dragonchang.domain.dto.UpwardTrendPageRequestDTO;
import com.dragonchang.domain.po.NewUpwardTrend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewUpwardTrendMapper extends BaseMapper<NewUpwardTrend> {
    /**
     *
     * @param page
     * @param request
     * @return
     */
    IPage<NewUpwardTrendDTO> findPage(Page page, @Param("request") UpwardTrendPageRequestDTO request);

    /**
     * 查询列表
     * @param request
     * @return
     */
    List<NewUpwardTrendDTO> findList(@Param("request") UpwardTrendPageRequestDTO request);
}
