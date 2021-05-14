package com.dragonchang.domain.dto;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-14 16:02
 **/
@Data
public class HolderDetailRequestDTO  extends PageRequestDTO{
    /**
     * 持有者名稱
     */
    private String name;

}
