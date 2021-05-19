package com.dragonchang.domain.dto;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-19 18:30
 **/
@Data
public class FinanceAnalysisRequestDTO extends PageRequestDTO{
    String name;
    String stockCode;
    String order;
    String reportTime;
}
