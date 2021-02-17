package com.dragonchang.domain.dto.tyc;

import lombok.Data;

/**
 * @program: webcrawler
 * @description: 天眼查股权穿透name返回体
 * @author: zhangfl
 * @create: 2021-02-17 13:17
 **/
@Data
public class NameDto {
    String name;
    Long uv;
    Long pv;
    String v;
}
