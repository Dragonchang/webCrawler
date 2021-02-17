package com.dragonchang.domain.dto.tyc;

import lombok.Data;

/**
 * @program: webcrawler
 * @description: 股权穿透公司信息
 * @author: zhangfl
 * @create: 2021-02-17 21:27
 **/
@Data
public class ShareCompanyDto {
    /**
     * 持有公司股份金额
     */
    String amount;

    String companyType;

    /**
     * 持有公司股份比率
     */
    String percentFloat;

    /**
     * 持有公司股份名称
     */
    String name;

    /**
     * 公司股票类型
     */
    String bondType;

    /**
     *
     */
    String financeLabel;
    /**
     * 持有公司id
     */
    Long id;

    Long source;
    String hasNode;
    String type;
    /**
     * 百分比
     */
    String percent;

    /**
     * 公司品牌
     */
    String brand;
}
