package com.dragonchang.domain.dto;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-11 12:13
 **/
@Data
public class FocusAddRequestDTO {
    /**
     * 股份公司id
     */
    private String stockCompanyId;

    /**
     * 公司股票代码
     */
    private String stockCode;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 关注类型 1.股份公司 2.机构 3.个人
     */
    private String type;
}
