package com.dragonchang.domain.dto;

import lombok.Data;

@Data
public class StrategyValidateRequestDTO {
    private Long id;
    private String scriptType;
    private String scriptContent;
}

