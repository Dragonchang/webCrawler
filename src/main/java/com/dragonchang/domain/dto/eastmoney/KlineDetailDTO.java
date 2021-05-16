package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

@Data
public class KlineDetailDTO {
    private String code;
    private String name;
    private String [] klines;
}
