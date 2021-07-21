package com.dragonchang.service;

public interface IUpwardTrendService {
    /**
     * 生成当天上升趋势的票子
     */
    void generateUpwardTrendListByToday(String today);
}
