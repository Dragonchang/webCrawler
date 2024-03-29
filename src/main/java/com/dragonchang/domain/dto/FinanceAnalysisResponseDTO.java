package com.dragonchang.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-20 14:35
 **/
@Data
public class FinanceAnalysisResponseDTO {

    private Integer id;

    /**
     * t_company_stock主键
     */
    private Integer stockCompanyId;

    /**
     * 公司股票代码
     */
    private String stockCode;

    /**
     * 公司名称
     */
    private String name;

    /**
     * 公司最新股价
     */
    private BigDecimal lastPrice;

    /**
     * 当天涨幅%
     */
    private String dtzf;

    /**
     * 当天成交量（手）
     */
    private String dtcjl;

    /**
     * 当天成交金额（元）
     */
    private BigDecimal dtcjje;

    /**
     * 当天换手率%
     */
    private String dthsl;

    /**
     * 量比
     */
    private String lb;


    /**
     * 市盈率%
     */
    private String syl;
    /**
     * 公司最新总市值
     */
    private BigDecimal totalCapitalization;

    /**
     * 季度总营收
     */
    private BigDecimal totalIncome;

    /**
     * 营收市值百分比
     */
    private BigDecimal incomeTotalPercent;

    /**
     * 总营收同比增长
     */
    private BigDecimal totalAddPercent;

    /**
     * 扣非利润
     */
    private BigDecimal netProfit;

    /**
     * 扣非同比增长
     */
    private BigDecimal netProfitPercent;

    /**
     * 扣非营收百分比
     */
    private BigDecimal profitTotalPercent;

    /**
     * 财务信息发布季度
     */
    private String reportTime;

    /**
     * 报告类型(1一季度报，2中报，3三季度报，4 年报)
     */
    private String reportType;

    /**
     * 公司板块信息
     */
    private String bkInfo;

    /**
     * 公司概念板块信息
     */
    private String conceptInfo;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime updatedTime;

}
