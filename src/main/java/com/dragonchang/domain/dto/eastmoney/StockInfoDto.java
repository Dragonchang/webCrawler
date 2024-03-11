package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-23 13:42
 **/
@Data
public class StockInfoDto {

    /**
     * 股票代码
     */
    private String f12;
    /**
     * 股票名称
     */
    private String f14;
    /**
     * 股票最新价格
     */
    private String f2;

    /**
     * 当天涨幅
     */
    private String f3;

    /**
     * 当天成交量（手）
     */
    private String f5;

    /**
     * 当天成交金额（元）
     */
    private String f6;

    /**
     * 当天换手率
     */
    private String f8;

    /**
     * 量比
     */
    private String f10;

    /**
     * 昨天股价
     */
    private String f18;

    /**
     * 上市时间
     */
    private String f26;

    /**
     * 市盈率
     */
    private String f115;
}
