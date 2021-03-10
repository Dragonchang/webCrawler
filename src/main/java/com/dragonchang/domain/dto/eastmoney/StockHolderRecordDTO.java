package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-10 09:26
 **/
@Data
public class StockHolderRecordDTO {
    /**
     * 发布时间
     */
    private String rq;

    /**
     * 流通股东列表
     */
    private List<StockHolderDetailDTO> sdltgd;

    /**
     * 股东列表
     */
    private List<StockHolderDetailDTO> sdgd;
}
