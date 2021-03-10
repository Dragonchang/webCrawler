package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-10 09:27
 **/
@Data
public class StockHolderRecordListDTO {
    /**
     * 流通股东列表
     */
    private List<StockHolderRecordDTO> sdltgd;

    /**
     * 股东列表
     */
    private List<StockHolderRecordDTO> sdgd;
}
