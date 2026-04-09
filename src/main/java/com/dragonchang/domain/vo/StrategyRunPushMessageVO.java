package com.dragonchang.domain.vo;

import lombok.Data;

@Data
public class StrategyRunPushMessageVO {
    private Long runId;
    private Long strategyId;
    private String eventType;
    private String status;
    private Integer progress;
    private String message;
    private String logLevel;
    private String logTime;
    private Integer resultCount;
    private String stageCode;
    private Integer current;
    private Integer total;
}
