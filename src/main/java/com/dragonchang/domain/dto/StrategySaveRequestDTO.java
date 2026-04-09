package com.dragonchang.domain.dto;

import lombok.Data;

/**
 * 策略保存请求
 */
@Data
public class StrategySaveRequestDTO {
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
    private String scheduleType;
    private String cronExpr;
}

