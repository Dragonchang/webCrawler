package com.dragonchang.domain.dto;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-14 16:02
 **/
@Data
public class HolderDetailRequestDTO extends PageRequestDTO{
    /**
     * 持有者名稱
     */
    private String name;

    /**
     * 持有公司的名称
     */
    private String companyName;

    /**
     * 持有公司股票代码
     */
    private String companyStock;

    /**
     * 持有次数
     */
    private Integer count;

    /**
     * 发布时间
     */
    private String reportTime;

    /**
     * <option value="1" >${"增加"}</option>
     * <option value="2" >${"减少"}</option>
     * <option value="3" >${"不变"}</option>
     */
    private String change;
}
