package com.dragonchang.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-23 16:25
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_company_stock")
public class CompanyStock {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 天眼查公司id
     */
    private String companyId;

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
     * 公司最新总市值
     */
    private BigDecimal totalCapitalization;

    /**
     * 公司最新流通市值
     */
    private BigDecimal lastCirculation;

    /**
     * 公司最新收益
     */
    private BigDecimal lastIncome;

    /**
     * 上市时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime marketTime;


    /**
     * 当天涨幅%
     */
    private String dtzf;

    /**
     * 当天成交量（手）
     */
    private String dtcjl;

    /**
     * 当天成交金额（元）
     */
    private BigDecimal dtcjje;

    /**
     * 当天换手率%
     */
    private String dthsl;

    /**
     * 量比
     */
    private String lb;


    /**
     * 市盈率%
     */
    private String syl;

    /**
     * 是否删除，0表示未删除，1表示删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime createdTime;

    /**
     * 创建人员
     */
    private String createdBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime updatedTime;

    /**
     * 更新人员
     */
    private String updatedBy;

}
