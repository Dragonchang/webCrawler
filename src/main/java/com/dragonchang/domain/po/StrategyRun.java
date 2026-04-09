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
 * 策略运行实例表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_strategy_run")
public class StrategyRun {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long strategyId;
    private Long strategyVersionId;
    private String runType;
    private String triggerSource;
    private String runStatus;
    private String paramSnapshot;
    private String dataSnapshotDate;
    private String universeSnapshot;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    private Long durationMs;
    private Integer resultCount;
    private String errorMessage;
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;
}

