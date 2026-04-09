package com.dragonchang.domain.dto;

import lombok.Data;

/**
 * 策略分页请求
 */
@Data
public class StrategyRequestDTO extends PageRequestDTO {
    private String strategyName;
    private String strategyCode;
    private String category;
    private Integer status;
}

