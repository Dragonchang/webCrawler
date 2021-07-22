package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.UpwardTrendDTO;
import com.dragonchang.domain.dto.UpwardTrendPageRequestDTO;
import com.dragonchang.domain.po.CompanyStock;

public interface IUpwardTrendService {
    /**
     * 生成当天上升趋势的票子
     */
    void generateUpwardTrendListByToday(String today);

    /**
     * 分页查询当日趋势信息
     *
     * @param pageRequest
     * @return
     */
    IPage<UpwardTrendDTO> findPage(UpwardTrendPageRequestDTO pageRequest);

    /**
     * 导出公司信息
     * @param request
     * @return
     */
    ExcelData exportFlow(UpwardTrendPageRequestDTO request);
}
