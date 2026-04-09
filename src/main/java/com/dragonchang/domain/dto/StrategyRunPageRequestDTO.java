package com.dragonchang.domain.dto;

import lombok.Data;

/**
 * 策略运行分页请求
 */
@Data
public class StrategyRunPageRequestDTO extends PageRequestDTO {
    private Long strategyId;
    private String strategyName;
    private String runStatus;
}

