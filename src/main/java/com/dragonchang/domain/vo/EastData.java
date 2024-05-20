package com.dragonchang.domain.vo;

import lombok.Data;

@Data
public class EastData<T> {
    Integer pages;
    private T data;
}
