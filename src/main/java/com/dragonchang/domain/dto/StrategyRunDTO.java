package com.dragonchang.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 策略运行展示对象
 */
@Data
public class StrategyRunDTO {
    private Long id;
    private Long strategyId;
    private Long strategyVersionId;
    private String strategyName;
    private String strategyCode;
    private Integer versionNo;
    private String engineType;
    private String scriptType;
    private String runType;
    private String triggerSource;
    private String runStatus;
    private String dataSnapshotDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationMs;
    private Integer resultCount;
    private String errorMessage;
    private LocalDateTime createdTime;
}

