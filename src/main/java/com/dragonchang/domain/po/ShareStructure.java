package com.dragonchang.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @program: webcrawler
 * @description: 公司持有其它公司股份信息
 * @author: zhangfl
 * @create: 2021-02-18 11:42
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_share_structure")
public class ShareStructure {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公司信息主键
     */
    private Long companyId;

    /**
     * 持有股权公司名称
     */
    private String shareCompanyName;

    /**
     * 参股公司股票代码
     */
    private String shareCompanyStockCode;

    /**
     * 参股金额
     */
    private String shareCompanyAmount;

    /**
     * 公司类型
     */
    private String shareCompanyType;

    /**
     * 参股公司债券类型
     */
    private String shareCompanyBondType;

    /**
     * 股权类型
     */
    private String shareCompanyFinanceLabel;

    /**
     * 参股比例
     */
    private String shareCompanyPercent;

    /**
     * 参股公司品牌
     */
    private String shareCompanyBrand;

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
}
