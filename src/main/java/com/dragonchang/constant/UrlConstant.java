package com.dragonchang.constant;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @Author: chenyanfeng
 * @Date: 2018-11-23
 * @Time: 下午4:49
 */
public class UrlConstant {


    /**
     * 公司搜索URL
     */
    public static final String SEARCH_RUL = "https://capi.tianyancha.com/cloud-equity-provider/v4/equity/indexnode.json";


    /**
     * 获取企业基本信息
     */
    public static final String BASE_INFO_URL = "http://open.api.tianyancha.com/services/open/ic/baseinfo/2.0";

    /**
     * 获取股权穿透
     */
    public static final String Share_Name_URL = "https://capi.tianyancha.com/cloud-equity-provider/v4/qq/name.json";
    public static final String Share_Node_URL = "https://capi.tianyancha.com/cloud-equity-provider/v4/equity/indexnode.json";

    /**
     * 东方财富获取所有股票列表
     */
    public static final String Stock_Info_URL = "http://88.push2.eastmoney.com/api/qt/clist/get";

    /**
     * 东方财富获取单个股票信息
     */
    public static final String Stock_Detail_Info_URL = "http://push2.eastmoney.com/api/qt/stock/get";


    /**
     * 东方财富获取公司持股股东信息
     */
    public static final String Stock_Holder_Info_URL = "http://f10.eastmoney.com/ShareholderResearch/ShareholderResearchAjax";

    /**
     * 东方财富获取k线数据
     */
    public static final String Stock_Kline_Info_URL = "http://push2his.eastmoney.com/api/qt/stock/kline/get";

    /**
     * 东方财富获取财报发布日期
     */
    public static final String Finance_Analysis_Time_URL = "http://f10.eastmoney.com/NewFinanceAnalysis/lrbDateAjaxNew";

    /**
     * 东方财富获取财报发布详细信息
     */
    public static final String Finance_Analysis_Result_URL = "http://f10.eastmoney.com/NewFinanceAnalysis/lrbAjaxNew";


    /**
     * 东方财富获取所有板块列表
     * http://quote.eastmoney.com/center/boardlist.html#industry_board
     */
    public static final String BK_Info_URL = "https://5.push2.eastmoney.com/api/qt/clist/get";

    /**
     * 东方财富获取所有板块列表
     * https://data.eastmoney.com/bkzj/BK1015.html
     */
    public static final String BK_Stock_Info_URL = "https://push2.eastmoney.com/api/qt/clist/get";


    /**
     * 东方财富获取所有板块列表
     * http://quote.eastmoney.com/center/boardlist.html#concept_board
     */
    public static final String Concept_Info_URL = "http://91.push2.eastmoney.com/api/qt/clist/get";

    /**
     * 东方财富获取所有板块列表
     * https://data.eastmoney.com/bkzj/BK1141.html
     */
    public static final String Concept_Stock_Info_URL = "https://push2.eastmoney.com/api/qt/clist/get";
}
