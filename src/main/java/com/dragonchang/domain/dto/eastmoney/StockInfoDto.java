package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

/**
 * @program: webcrawler
 * @description: 东方财富股票列表返回字段
 * @author: zhangfl
 * @create: 2021-02-23 13:42
 **/
@Data
public class StockInfoDto {

    /**
     * 未知/序号标识
     */
    private String f1;

    /**
     * 最新价
     */
    private String f2;

    /**
     * 涨跌幅（%）
     */
    private String f3;

    /**
     * 涨跌额
     */
    private String f4;

    /**
     * 成交量（手）
     */
    private String f5;

    /**
     * 成交额（元）
     */
    private String f6;

    /**
     * 振幅（%）
     */
    private String f7;

    /**
     * 换手率（%）
     */
    private String f8;

    /**
     * 市盈率（动态）
     */
    private String f9;

    /**
     * 量比
     */
    private String f10;

    /**
     * 5分钟涨跌幅/分时涨跌
     */
    private String f11;

    /**
     * 股票代码
     */
    private String f12;

    /**
     * 市场标识（0: 深/北交所，1: 沪市等，按东方财富返回为准）
     */
    private String f13;

    /**
     * 股票名称
     */
    private String f14;

    /**
     * 最高价
     */
    private String f15;

    /**
     * 最低价
     */
    private String f16;

    /**
     * 今开
     */
    private String f17;

    /**
     * 昨收
     */
    private String f18;

    /**
     * 总市值
     */
    private String f20;

    /**
     * 流通市值
     */
    private String f21;

    /**
     * 涨速
     */
    private String f22;

    /**
     * 市净率
     */
    private String f23;

    /**
     * 60日涨跌幅
     */
    private String f24;

    /**
     * 年初至今涨跌幅
     */
    private String f25;

    /**
     * 上市日期，格式如 20211115
     */
    private String f26;

    /**
     * 未知/保留字段
     */
    private String f27;

    /**
     * 未知/保留字段
     */
    private String f28;

    /**
     * 未知/保留字段
     */
    private String f29;

    /**
     * 未知/保留字段
     */
    private String f30;

    /**
     * 市盈率（动态）扩展返回字段
     */
    private String f115;

    /**
     * 最新总市值
     */
    private String f116;

    /**
     * 最新流通市值
     */
    private String f117;

    /**
     * 委比相关扩展字段，当前返回常见为“-”
     */
    private String f128;

    /**
     * DRG/板块等扩展字段，当前返回常见为“-”
     */
    private String f136;

    /**
     * 扩展字段，当前返回常见为“-”
     */
    private String f140;

    /**
     * 扩展字段，当前返回常见为“-”
     */
    private String f141;

    /**
     * 状态标识/返回状态码
     */
    private String f152;
}
