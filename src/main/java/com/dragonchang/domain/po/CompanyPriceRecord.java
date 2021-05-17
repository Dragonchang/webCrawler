package com.dragonchang.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-17 19:16
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_company_price_record")
public class CompanyPriceRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * t_company_stock主键
     */
    private Integer companyStockId;

    private String openPrice;
    private String closePrice;
    private String highestPrice;
    private String lowestPrice;
    /**
     * 股东信息发布时间
     */
    private String reportTime;
    /**
     * 是否删除，0表示未删除，1表示删除
     */
    private Integer deleted;
    /**
     * 创建人员
     */
    private String createdBy;
}
