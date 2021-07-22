package com.dragonchang.domain.dto;

import com.dragonchang.domain.po.UpwardTrend;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-07-21 15:04
 **/
@Data
public class UpwardTrendDTO extends UpwardTrend {
    /**
     * 公司名称
     */
    private String name;

    /**
     * 公司股票代码
     */
    private String stockCode;

    /**
     * 公司最新股价
     */
    private BigDecimal lastPrice;
}
