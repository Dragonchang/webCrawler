package com.dragonchang.crawler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dragonchang.constant.UrlConstant;
import com.dragonchang.domain.dto.eastmoney.*;
import com.dragonchang.domain.vo.TycResult;
import com.dragonchang.tianyancha.HeaderUtils;
import com.dragonchang.tianyancha.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: webcrawler
 * @description: 东方财富
 * @author: zhangfl
 * @create: 2021-02-23 13:40
 **/
@Slf4j
@Component
public class EastMoneyCrawler {

    /**
     * 获取沪深A股公司信息列表
     * @return
     */
    public List<StockInfoDto> getStockList() {
        Map<String, String> params = new HashMap<>();
        params.put("pn", "1");
        params.put("pz", "5000");
        params.put("po", "1");
        params.put("np", "1");
        params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "f3");
        params.put("fs", "m:0+t:6,m:0+t:13,m:0+t:80,m:1+t:2,m:1+t:23");
        params.put("fields", "f2,f12,f14,f18,f26");
        String result = HttpClientUtils.doGetForString(UrlConstant.Stock_Info_URL,
                HeaderUtils.getEastMoneyWebHeaders(), params);
        TycResult<StockInfoListDto> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<StockInfoListDto>>() {
        });
        return eastMoneyResult.getData().getDiff();
    }

    /**
     * 获取公司股票详细信息
     * @return
     */
    private static  String detailFields = "f43,f44,f45,f46,f47,f48,f49,f50,f51,f52,f55,f57,f58,f60,f62,f168,f169,f170,f164,f163,f116,f167,f117,f71,f161,f530,f135,f136,f137,f138,f139,f141,f142,f144,f145,f147,f148,f140,f143,f146,f149,f162,f92,f173,f104,f105,f84,f85,f183,f184,f185,f186,f187,f188,f189,f190,f191,f192,f107,f111,f86,f177,f78,f110,f262,f263,f264,f267,f268,f250,f251,f252,f253,f254,f255,f256,f257,f258,f266,f269,f270,f271,f273,f274,f275,f127,f199,f128,f198,f259,f260,f261,f171,f277,f278,f279,f288,f292,f182";
    public StockDetailDto getStockInfoByStockCode(String stockCode) {
        Map<String, String> params = new HashMap<>();
        params.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("volt", "2");
        if(stockCode.startsWith("300") || stockCode.startsWith("00")) {
            params.put("secid", "0." + stockCode);
        } else if(stockCode.startsWith("6")) {
            params.put("secid", "1." + stockCode);
        }
        params.put("fields", detailFields);
        String result = HttpClientUtils.doGetForString(UrlConstant.Stock_Detail_Info_URL,
                HeaderUtils.getEastMoneyWebHeaders(), params);
        TycResult<StockDetailDto> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<StockDetailDto>>() {
        });
        if(eastMoneyResult == null) {
            return null;
        }
        return eastMoneyResult.getData();
    }


    /**
     * 获取公司股股东细信息
     * @return
     */
    public StockHolderRecordListDTO getStockHodlerInfoByCode(String stockCode) {
        Map<String, String> params = new HashMap<>();
        if(stockCode.startsWith("300") || stockCode.startsWith("00")) {
            stockCode = "sz"+stockCode;
        } else if(stockCode.startsWith("6")) {
            stockCode = "sh"+stockCode;
        }
        params.put("code", stockCode);
        String result = HttpClientUtils.doGetForString(UrlConstant.Stock_Holder_Info_URL,
                HeaderUtils.getEastMoneyHolderHeaders(stockCode), params);
        StockHolderRecordListDTO eastMoneyResult = JSONObject.parseObject(result, StockHolderRecordListDTO.class);
        return eastMoneyResult;
    }

    /**
     * 获取历史股价的k线
     */
    private static  String klineFields = "f1,f2,f3,f4,f5,f6";
    private static  String klineFields2 = "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61";
    public KlineDetailDTO getKlineData(String stockCode) {
        Map<String, String> params = new HashMap<>();
        params.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
        params.put("klt", "101");
        params.put("fqt", "1");
        params.put("beg", "0");
        params.put("end", "20500000");
        if(stockCode.startsWith("300") || stockCode.startsWith("00")) {
            params.put("secid", "0." + stockCode);
        } else if(stockCode.startsWith("6")) {
            params.put("secid", "1." + stockCode);
        }
        params.put("fields1", klineFields);
        params.put("fields2", klineFields2);
        String result = HttpClientUtils.doGetForString(UrlConstant.Stock_Kline_Info_URL,
                HeaderUtils.getEastMoneyWebHeaders(), params);
        TycResult<KlineDetailDTO> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<KlineDetailDTO>>() {
        });
        if(eastMoneyResult == null) {
            return null;
        }
        return eastMoneyResult.getData();
    }

    /**
     * 获取公司当天的股价
     * @param stockCode
     * @return
     */
    public TodayPriceDTO getTodayPrice(String stockCode) {
        Map<String, String> params = new HashMap<>();
        params.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("volt", "2");
        if(stockCode.startsWith("300") || stockCode.startsWith("00")) {
            params.put("secid", "0." + stockCode);
        } else if(stockCode.startsWith("6")) {
            params.put("secid", "1." + stockCode);
        }
        params.put("fields", detailFields);
        String result = HttpClientUtils.doGetForString(UrlConstant.Stock_Detail_Info_URL,
                HeaderUtils.getEastMoneyWebHeaders(), params);
        TycResult<TodayPriceDTO> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<TodayPriceDTO>>() {
        });
        if(eastMoneyResult == null) {
            return null;
        }

        return eastMoneyResult.getData();
    }

    /**
     * 获取财报发布时间表
     * @param stockCode
     */
    public List<FinanceReportTimeDTO> getFinanceReport(String stockCode) {
        Map<String, String> params = new HashMap<>();
        if(stockCode.startsWith("300") || stockCode.startsWith("00")) {
            stockCode = "SZ"+stockCode;
        } else if(stockCode.startsWith("6")) {
            stockCode = "SH"+stockCode;
        }

        params.put("companyType", "4");
        params.put("reportDateType", "0");
        params.put("code", stockCode);
        String result = HttpClientUtils.doGetForString(UrlConstant.Finance_Analysis_Time_URL,
               null, params);
        TycResult<List<FinanceReportTimeDTO>> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<List<FinanceReportTimeDTO>>>() {
        });
        if(eastMoneyResult == null) {
            return null;
        }
        return eastMoneyResult.getData();
    }


    /**
     * 获取财报发布详细信息
     * @param stockCode
     */
    public List<FinanceAnalysisDataDTO> getFinanceAnalysisData(String dates, String stockCode) {
        Map<String, String> params = new HashMap<>();
        if(stockCode.startsWith("300") || stockCode.startsWith("00")) {
            stockCode = "SZ"+stockCode;
        } else if(stockCode.startsWith("6")) {
            stockCode = "SH"+stockCode;
        }

        params.put("companyType", "4");
        params.put("reportDateType", "0");
        params.put("reportType", "1");
        params.put("dates", dates);
        params.put("code", stockCode);
        String result = HttpClientUtils.doGetForString(UrlConstant.Finance_Analysis_Result_URL,
                null, params);
        TycResult<List<FinanceAnalysisDataDTO>> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<List<FinanceAnalysisDataDTO>>>() {
        });
        if(eastMoneyResult == null) {
            return null;
        }
        return eastMoneyResult.getData();
    }


    public static void main(String[] args) {
        EastMoneyCrawler tycCrawler = new EastMoneyCrawler();
        //List<StockInfoDto> list = tycCrawler.getStockList();

       // StockDetailDto detailDto = tycCrawler.getStockInfoByStockCode("603893");

        List<FinanceReportTimeDTO> ret = tycCrawler.getFinanceReport("300940");

        //List<FinanceAnalysisDataDTO> data = tycCrawler.getFinanceAnalysisData("2021-03-31,2020-12-31,2020-09-30,2020-06-30,2020-03-31","603456");
        log.info("test");
    }
}
