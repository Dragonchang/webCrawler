package com.dragonchang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-15 21:50
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.dragonchang.mapper")
@EnableScheduling
public class WebcrawlerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebcrawlerApplication.class, args);
    }
}
