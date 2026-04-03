package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

@Data
public class NewFinanceAnalysisDataDTO {
    /**
     * 总营收
     */
    private String TOTALOPERATEREVE;
    /**
     * 扣非净利润
     */
    private String KCFJCXSYJLR;

    /**
     * "2020-12-31 00:00:00"
     */
    private String REPORT_DATE;
    /**
     * "2020年报"
     */
    private String REPORT_DATE_NAME;

    /**
     * 股票代码
     */
    private String SECURITY_CODE;

    /**
     * "年报"
     */
    private String REPORT_TYPE;

    /**
     * "2021-04-28 00:00:00"
     */
    private String UPDATE_DATE;
}
