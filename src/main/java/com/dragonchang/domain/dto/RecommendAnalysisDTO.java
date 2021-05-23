package com.dragonchang.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecommendAnalysisDTO {
    /**
     * 季度总营收阈值
     * 1. 大于100
     * 2. 50~100亿
     * 3. 10亿~50亿
     */
    private BigDecimal totalIncomeThreshold;

    /**
     * 总营收同比增长
     * 1. 大于100  5%
     * 2. 50~100亿 10%
     * 3. 10亿~50亿 40%
     */
    private BigDecimal totalAddPercentThreshold;

    /**
     * 扣非利润
     */
    private BigDecimal netProfitThreshold;

    /**
     * 扣非同比增长
     */
    private BigDecimal netProfitPercentThreshold;

    /**
     * 财务信息发布季度
     */
    private String reportTime;
}
