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
 * 策略版本表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_strategy_version")
public class StrategyVersion {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long strategyId;
    private Integer versionNo;
    private String scriptType;
    private String scriptContent;
    private String paramSchema;
    private String defaultParams;
    private String universeConfig;
    private String scriptHash;
    private Integer isPublished;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;
    private String createdBy;
}

