package com.dragonchang.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-12 13:52
 **/
@Data
public class HolderCompanyListDTO {
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

    /**
     * 公司最新流通市值
     */
    private BigDecimal lastCirculation;

    /**
     * 公司最新总市值
     */
    private BigDecimal totalCapitalization;

    /**
     * 公司最新收益
     */
    private BigDecimal lastIncome;

    /**
     * 股东排名
     */
    private Long holderRank;

    /**
     * 持股人/机构名称
     */
    private String holderName;

    /**
     * 持股数(股)
     */
    private Long holdCount;

    /**
     * 占总流通/总股本持股比例
     */
    private String holdPercent;

    /**
     * 增减
     */
    private String zj;

    /**
     * 变动比例
     */
    private String changePercent;

    /**
     * 股东类型(1-股东，2-流通股东)
     */
    private String holderType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime createdTime;

    /**
     * 股东信息发布时间
     */
    private String reportTime;
}
