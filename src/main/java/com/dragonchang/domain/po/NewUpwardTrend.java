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
 * @create: 2021-07-23 13:17
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_new_upward_trend")
public class NewUpwardTrend {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * t_company_stock主键
     */
    private Integer companyStockId;

    /**
     * 上涨态势生成时间
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
