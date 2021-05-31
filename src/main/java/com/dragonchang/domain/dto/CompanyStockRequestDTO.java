package com.dragonchang.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-02 18:02
 **/
@Data
public class CompanyStockRequestDTO extends PageRequestDTO{
    String name;
    String stockCode;
    String order;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String marketTime;
}
