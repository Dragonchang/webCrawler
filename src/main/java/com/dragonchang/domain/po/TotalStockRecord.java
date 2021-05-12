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
 * @create: 2021-03-08 15:54
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_total_stock_record")
public class TotalStockRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所有公司股票平均价格
     */
    private BigDecimal averagePrice;

    /**
     * 所有公司总市值
     */
    private BigDecimal totalCapitalization;

    /**
     * 所有公司流通市值
     */
    private BigDecimal lastCirculation;

    /**
     * 是否删除，0表示未删除，1表示删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime recordTime;

    /**
     * 创建人员
     */
    private String createdBy;
}
