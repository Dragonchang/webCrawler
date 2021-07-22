package com.dragonchang.domain.dto;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-07-22 14:26
 **/
@Data
public class UpwardTrendPageRequestDTO extends PageRequestDTO{
    String today;
    String name;
    String stockCode;
    String filter;
    String isHeight;
}
