package com.dragonchang.domain.dto.tyc;

import com.dragonchang.domain.dto.PageRequestDTO;
import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-18 11:49
 **/
@Data
public class CompanyRequestDTO extends PageRequestDTO {
    String tycId;
    String companyName;
    String stockCode;
}
