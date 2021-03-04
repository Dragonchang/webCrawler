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
    public static final String Stock_Info_URL = "http://16.push2.eastmoney.com/api/qt/clist/get";

    /**
     * 东方财富获取单个股票信息
     */
    public static final String Stock_Detail_Info_URL = "http://push2.eastmoney.com/api/qt/stock/get";
}
