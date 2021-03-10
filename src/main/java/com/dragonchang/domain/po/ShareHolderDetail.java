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
 * @description:
 * @author: zhangfl
 * @create: 2021-03-10 08:32
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_share_holder_detail")
public class ShareHolderDetail {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * t_company_share_holder主键
     */
    private Long holderId;

    /**
     * 股东排名
     */
    private Long holderRank;

    /**
     * 持股人/机构名称
     */
    private String holderName;

    /**
     * 股东性质
     */
    private String holderType;

    /**
     * 股份类型
     */
    private String stockType;

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
