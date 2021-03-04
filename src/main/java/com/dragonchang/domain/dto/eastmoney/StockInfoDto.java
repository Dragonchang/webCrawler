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
     * 昨天股价
     */
    private String f18;
}
