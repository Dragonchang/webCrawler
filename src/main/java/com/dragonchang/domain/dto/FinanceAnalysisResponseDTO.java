package com.dragonchang.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-20 14:35
 **/
public class FinanceAnalysisResponseDTO {

    private Integer id;

    /**
     * t_company_stock主键
     */
    private Integer stockCompanyId;

    /**
     * 公司名称
     */
    private String name;

    /**
     * 公司最新股价
     */
    private BigDecimal lastPrice;

    /**
     * 季度总营收
     */
    private BigDecimal totalIncome;

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
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime updatedTime;

}
