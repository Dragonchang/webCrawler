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
 * @create: 2021-05-19 13:35
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_finance_analysis")
public class FinanceAnalysis {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * t_company_stock主键
     */
    private Integer stockCompanyId;

    /**
     * 季度总营收
     */
    private BigDecimal totalIncome;

    /**
     * 总营收同比增长
     */
    private BigDecimal totalAddPercent;

    /**
     * 扣非利润
     */
    private BigDecimal netProfit;

    /**
     * 扣非同比增长
     */
    private BigDecimal netProfitPercent;

    /**
     * 财务信息发布季度
     */
    private String reportTime;

    /**
     * 报告类型(1一季度报，2中报，3三季度报，4 年报)
     */
    private String reportType;

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
