package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-18 17:43
 **/
@Data
public class TodayPriceDTO {

    /**
     * open price
     */
    private String f46;
    /**
     * close price
     */
    private String f43;
    /**
     * 最高价
     */
    private String f44;

    /**
     * 最低价
     */
    private String f45;
}
