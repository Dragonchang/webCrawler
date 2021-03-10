package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-10 09:17
 **/
@Data
public class StockHolderDetailDTO {
    /**
     * 公布时间
     */
    private String rq;

    /**
     * 名次
     */
    private String mc;

    /**
     * 股东名称
     */
    private String gdmc;

    /**
     * 股东性质
     */
    private String gdxz;

    /**
     * 股份类型
     */
    private String gflx;

    /**
     * 持股数(股)
     */
    private String cgs;

    /**
     * 占总流通股本持股比例
     */
    private String zltgbcgbl;

    /**
     * 增减(股)
     */
    private String zj;

    /**
     * 变动比例
     */
    private String bdbl;
}
