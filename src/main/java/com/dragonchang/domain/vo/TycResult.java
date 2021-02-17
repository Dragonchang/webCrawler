package com.dragonchang.domain.vo;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-17 13:12
 **/

@Data
public class TycResult<T> {

    public static final String SUCCESS="ok";

    private String state;

    private String message;

    private String special;

    private String vipMessage;

    private boolean isLogin;

    private T data;
}
