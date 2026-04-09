package com.dragonchang.domain.dto;

import lombok.Data;

/**
 * 策略执行结果展示对象
 */
@Data
public class StrategyResultDTO {
    private Long id;
    private Long runId;
    private Integer stockId;
    private String stockCode;
    private String stockName;
    private String actionType;
    private String score;
    private String reason;
    private String factorDetail;
    private Integer rankNo;
}

