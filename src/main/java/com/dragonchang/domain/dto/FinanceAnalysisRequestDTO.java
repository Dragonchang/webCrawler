package com.dragonchang.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-19 18:30
 **/
@Data
public class FinanceAnalysisRequestDTO extends PageRequestDTO{
    String name;
    String stockCode;
    String order;
    String reportTime;

    /**
     * 公司最新总市值
     */
    BigDecimal totalCapitalization;

    /**
     * 总营收同比增长
     */
    BigDecimal totalAddPercent;

    /**
     * 扣非同比增长
     */
    BigDecimal netProfitPercent;

    /**
     * 扣非
     */
    BigDecimal netProfit;

    String bkinfo;
}
