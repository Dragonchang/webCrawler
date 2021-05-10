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
 * @description: 关注列表
 * @author: zhangfl
 * @create: 2021-02-18 11:20
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_focus")
public class Focus {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 股份公司id
     */
    private String stockCompanyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 关注类型 1.股份公司 2.机构 3.个人
     */
    private String type;

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
