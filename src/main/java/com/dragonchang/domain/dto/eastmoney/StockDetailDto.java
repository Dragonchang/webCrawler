package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-05 10:27
 **/
@Data
public class StockDetailDto {

    /**
     * 最新股票价格
     */
    private String f43;

    /**
     * 最新收益
     */
    private String f55;

    /**
     * 每股净资产
     */
    private String f92;
    /**
     * 最新总市值
     */
    private String f116;

    /**
     * 最新流通市值
     */
    private String f117;

}
