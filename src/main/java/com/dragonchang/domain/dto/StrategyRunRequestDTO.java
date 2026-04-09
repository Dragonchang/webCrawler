package com.dragonchang.domain.dto;

import lombok.Data;

/**
 * 策略运行请求
 */
@Data
public class StrategyRunRequestDTO {
    private Long strategyId;
    private String params;
    private String dataSnapshotDate;
}

