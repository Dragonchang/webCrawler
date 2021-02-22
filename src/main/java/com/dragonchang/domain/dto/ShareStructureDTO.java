package com.dragonchang.domain.dto;

import com.dragonchang.domain.po.ShareStructure;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-22 10:45
 **/
@Data
public class ShareStructureDTO  extends ShareStructure {
    private BigDecimal amount;
    private BigDecimal percent;
}
