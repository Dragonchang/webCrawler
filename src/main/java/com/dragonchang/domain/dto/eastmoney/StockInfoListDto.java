package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-23 15:44
 **/
@Data
public class StockInfoListDto {
    private String total;
    private List<StockInfoDto> diff;
}
