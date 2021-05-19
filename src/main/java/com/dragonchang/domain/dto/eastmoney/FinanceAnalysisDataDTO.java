package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-19 11:16
 **/
@Data
public class FinanceAnalysisDataDTO {
    /**
     * 总营收
     */
    private String TOTAL_OPERATE_INCOME;
    /**
     * 扣非净利润
     */
    private String DEDUCT_PARENT_NETPROFIT;

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
