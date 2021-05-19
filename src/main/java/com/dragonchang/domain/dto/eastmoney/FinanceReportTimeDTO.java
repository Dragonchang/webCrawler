package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-19 10:38
 **/
@Data
public class FinanceReportTimeDTO {
    private String REPORT_DATE;
    private String REPORT_DATE_NAME;
    private String REPORT_TYPE;
    private String SECURITY_CODE;
}
