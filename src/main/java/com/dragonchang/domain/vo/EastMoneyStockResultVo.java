package com.dragonchang.domain.vo;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-23 12:48
 **/
@Data
public class EastMoneyStockResultVo<T> {
    private String rc;
    private String rt;
    private String svr;
    private String lt;
    private String full;
    private T data;
}
