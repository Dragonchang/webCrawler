package com.dragonchang.domain.dto.tyc;

import lombok.Data;

import java.util.List;

/**
 * @program: webcrawler
 * @description: 股权穿透列表
 * @author: zhangfl
 * @create: 2021-02-17 21:46
 **/
@Data
public class ShareCompanyListDto {
    String companyType;
    String name;
    Long id;
    Integer type;
    List<ShareCompanyDto> investorList;
}
