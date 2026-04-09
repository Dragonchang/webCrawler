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
 * 策略主表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_strategy_info")
public class StrategyInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String strategyCode;
    private String strategyName;
    private String category;
    private String description;
    private String scriptType;
    private String scriptContent;
    private String paramSchema;
    private String defaultParams;
    private String universeConfig;
    private Integer latestVersionNo;
    private Integer publishedVersionNo;
    private Integer status;
    private String scheduleType;
    private String cronExpr;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastRunTime;

    private String lastRunStatus;
    private Integer deleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedTime;
    private String updatedBy;
}

