package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2023-09-05 14:30
 **/
@Data
public class BKInfoListDTO {
    private String total;
    private List<BKInfoDTO> diff;
}
