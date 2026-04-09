package com.dragonchang.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 策略运行结果表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_strategy_result")
public class StrategyResult {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long runId;
    private Integer stockId;
    private String stockCode;
    private String stockName;
    private String actionType;
    private BigDecimal score;
    private String reason;
    private String factorDetail;
    private Integer rankNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;
}

