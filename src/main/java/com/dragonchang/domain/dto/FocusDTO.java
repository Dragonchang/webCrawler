package com.dragonchang.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FocusDTO {
    private Integer id;

    /**
     * 股份公司id
     */
    private String stockCompanyId;

    /**
     * 公司股票代码
     */
    private String stockCode;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 关注时候的价格
     */
    private BigDecimal focusPrice;

    /**
     * 公司最新股价
     */
    private BigDecimal lastPrice;

    /**
     * 差价
     */
    private BigDecimal diffPrice;

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
