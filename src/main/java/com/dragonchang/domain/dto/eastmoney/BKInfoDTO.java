package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2023-08-29 08:58
 **/
@Data
public class BKInfoDTO {
    /**
     * 最新点数
     */
    private String f2;
    /**
     * 今日装涨跌幅
     * 百分比
     */
    private String f3;

    /**
     * 今日装涨跌幅
     */
    private String f4;

    /**
     * 换手率
     */
    private String f8;

    /**
     * 板块代号
     */
    private String f12;


    /**
     * 板块名称
     */
    private String f14;

    /**
     * 总市值
     */
    private String f20;

    /**
     * 净流入
     */
    private String f62;
}
