package com.dragonchang.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2023-11-16 18:02
 **/
@Data
public class IncomeProfitListDTO {

    /**
     * 总营收
     */
    private BigDecimal totalIncome;


    /**
     * 扣非利润
     */
    private BigDecimal netProfit;

    /**
     * 财务信息发布季度
     */
    private String reportTime;
}
