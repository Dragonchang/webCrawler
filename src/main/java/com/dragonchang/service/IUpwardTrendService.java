package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.CompanyStockRequestDTO;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.NewUpwardTrendDTO;
import com.dragonchang.domain.dto.UpwardTrendDTO;
import com.dragonchang.domain.dto.UpwardTrendPageRequestDTO;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.NewUpwardTrend;

public interface IUpwardTrendService {
    /**
     * 生成当天上升趋势的票子
     */
    void generateUpwardTrendListByToday(String today);

    /**
     * 生成当天新进的上涨态势的股票
     * @param today
     */
    void generateNewUpwardTrendListByToday(String today);

    /**
     * 分页查询当日趋势信息
     *
     * @param pageRequest
     * @return
     */
    IPage<UpwardTrendDTO> findPage(UpwardTrendPageRequestDTO pageRequest);

    /**
     * 分页查询当日新增趋势信息
     *
     * @param pageRequest
     * @return
     */
    IPage<NewUpwardTrendDTO> newFindPage(UpwardTrendPageRequestDTO pageRequest);

    /**
     * 导出公司信息
     * @param request
     * @return
     */
    ExcelData exportFlow(UpwardTrendPageRequestDTO request);

    /**
     * 导出公司信息
     * @param request
     * @return
     */
    ExcelData newExportFlow(UpwardTrendPageRequestDTO request);
}
