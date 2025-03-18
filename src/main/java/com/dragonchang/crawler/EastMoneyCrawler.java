package com.dragonchang.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dragonchang.constant.UrlConstant;
import com.dragonchang.domain.dto.eastmoney.*;
import com.dragonchang.domain.vo.EastData;
import com.dragonchang.domain.vo.EastResult;
import com.dragonchang.domain.vo.TycResult;
import com.dragonchang.tianyancha.HeaderUtils;
import com.dragonchang.tianyancha.HttpClientUtils;
import com.dragonchang.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        List<StockInfoDto> data = new ArrayList<>();
        Integer pageSize = 0;
        Integer page = 1;
        Map<String, String> params = new HashMap<>();
        Integer page_number = 100;
        params.put("pn", "1");
        params.put("pz", "100");
        params.put("po", "1");
        params.put("np", "1");
        params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "f3");
        params.put("fs", "m:0 t:6,m:0 t:80,m:1 t:2,m:1 t:23,m:0 t:81 s:2048");
        params.put("fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,f20,f21,f22,f23,f24,f25,f26,f27,f28,f29,f30,f128,f136,f115,f152");
        String result = HttpClientUtils.doGetForString(UrlConstant.Stock_Info_URL,
                HeaderUtils.getEastStockListWebHeaders(), params);
        TycResult<StockInfoListDto> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<StockInfoListDto>>() {
        });
        data.addAll(eastMoneyResult.getData().getDiff());
        Integer total = Integer.parseInt(eastMoneyResult.getData().getTotal());
        pageSize = (total % page_number == 0)? (total / page_number): (total / page_number + 1);
        for(page = 2; page <= pageSize; ++page)
        {
            Map<String, String> params1 = new HashMap<>();

            params1.put("pn", page.toString());
            params1.put("pz", "100");
            params1.put("po", "1");
            params1.put("np", "1");
            params1.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
            params1.put("fltt", "2");
            params1.put("invt", "2");
            params1.put("fid", "f3");
            params1.put("fs", "m:0 t:6,m:0 t:80,m:1 t:2,m:1 t:23,m:0 t:81 s:2048");
            params1.put("fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152");
            String result1 = HttpClientUtils.doGetForString(UrlConstant.Stock_Info_URL,
                    HeaderUtils.getEastStockListWebHeaders(), params1);
            TycResult<StockInfoListDto> eastMoneyResult1 = JSONObject.parseObject(result1, new TypeReference<TycResult<StockInfoListDto>>() {
            });
            data.addAll(eastMoneyResult1.getData().getDiff());
        }
        return data;
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
        if(stockCode.startsWith("300") || stockCode.startsWith("00") || stockCode.startsWith("8")) {
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
     * 获取股东信息时间列表
     * @param stockCode
     * @return
     */
    public List<StockHolderDataDTO> getHolderData(String stockCode) {
        Map<String, String> params = new HashMap<>();
        if(stockCode.startsWith("300") || stockCode.startsWith("00")) {
            stockCode = stockCode+".SZ";
        } else if(stockCode.startsWith("6")) {
            stockCode = stockCode + ".SH";
        }
        params.put("reportName", "RPT_F10_EH_FREEHOLDERSDATE");
        params.put("columns", "SECUCODE,END_DATE");
        //params.put("quoteColumns", "");
        params.put("filter", "(SECUCODE=\""+stockCode+"\")");
        params.put("pageNumber", "1");
        //params.put("pageSize", "");
        params.put("sortTypes", "-1");
        params.put("sortColumns", "END_DATE");
        params.put("source", "HSF10");
        params.put("client", "PC");
        params.put("v", "04820951397256914");
        String result = HttpClientUtils.doGetForString(UrlConstant.Stock_Holder_Info_URL,
                HeaderUtils.getEastMoneyHolderDataHeaders(), params);
        EastResult result1 = JSONObject.parseObject(result, EastResult.class);
        if(result1 != null) {
            EastData result2 = result1.getResult();
            if(result2 != null) {
                JSONArray jsonArray = (JSONArray) result2.getData();
                if(jsonArray != null) {
                    String str = jsonArray.toJSONString();
                    List<StockHolderDataDTO> ret = JSONObject.parseObject(str, new TypeReference<List<StockHolderDataDTO>>() {
                    });
                    return ret;
                }
            }
        }
        return null;
    }
    /**
     * 获取公司流通股股东细信息
     * @return
     */
    public List<StockFreeHolderRecordDTO> getStockFreeHodlerInfoByCode(String stockCode, String data) {
        Map<String, String> params = new HashMap<>();
        if(stockCode.startsWith("300") || stockCode.startsWith("00")) {
            stockCode = stockCode+".SZ";
        } else if(stockCode.startsWith("6")) {
            stockCode = stockCode + ".SH";
        }
        params.put("reportName", "RPT_F10_EH_FREEHOLDERS");
        params.put("columns", "SECUCODE,SECURITY_CODE,END_DATE,HOLDER_RANK,HOLDER_NEW,HOLDER_NAME,HOLDER_TYPE,SHARES_TYPE,HOLD_NUM,FREE_HOLDNUM_RATIO,HOLD_NUM_CHANGE,CHANGE_RATIO");
        //params.put("quoteColumns", "");
        params.put("filter", "(SECUCODE=\""+stockCode+"\")"+"(END_DATE=\'" + data + "\')");
        params.put("pageNumber", "1");
        //params.put("pageSize", "");
        params.put("sortTypes", "1");
        params.put("sortColumns", "HOLDER_RANK");
        params.put("source", "HSF10");
        params.put("client", "PC");
        params.put("v", "006951842539512065");
        String result = HttpClientUtils.doGetForString(UrlConstant.Stock_Holder_Info_URL,
                HeaderUtils.getEastMoneyHolderDataHeaders(), params);
        if(result != null) {
            EastResult result1 = JSONObject.parseObject(result, EastResult.class);
            if(result1 != null) {
                EastData result3 = result1.getResult();
                if(result3 != null){
                    JSONArray jsonArray = (JSONArray) result3.getData();
                    if(jsonArray != null) {
                        String str = jsonArray.toJSONString();
                        List<StockFreeHolderRecordDTO> ret = JSONObject.parseObject(str, new TypeReference<List<StockFreeHolderRecordDTO>>() {
                        });
                        return ret;
                    }
                }
            }
        }
        return  null;
    }

    /**
     * 获取公司流通股股东细信息
     * @return
     */
    public List<StockNewHolderRecordDTO> getStockNewHodlerInfoByCode(String stockCode, String data) {
        Map<String, String> params = new HashMap<>();
        if(stockCode.startsWith("300") || stockCode.startsWith("00")) {
            stockCode = stockCode+".SZ";
        } else if(stockCode.startsWith("6")) {
            stockCode = stockCode + ".SH";
        }
        params.put("reportName", "RPT_F10_EH_HOLDERS");
        params.put("columns", "SECUCODE,SECURITY_CODE,END_DATE,HOLDER_RANK,HOLDER_NEW,HOLDER_NAME,SHARES_TYPE,HOLD_NUM,HOLD_NUM_RATIO,HOLD_NUM_CHANGE,CHANGE_RATIO");
        //params.put("quoteColumns", "");
        params.put("filter", "(SECUCODE=\""+stockCode+"\")"+"(END_DATE=\'" + data + "\')");
        params.put("pageNumber", "1");
        //params.put("pageSize", "");
        params.put("sortTypes", "1");
        params.put("sortColumns", "HOLDER_RANK");
        params.put("source", "HSF10");
        params.put("client", "PC");
        params.put("v", "006951842539512065");
        String result = HttpClientUtils.doGetForString(UrlConstant.Stock_Holder_Info_URL,
                HeaderUtils.getEastMoneyHolderDataHeaders(), params);

        EastResult result1 = JSONObject.parseObject(result, EastResult.class);
        if(result1 != null && result1.getResult() != null) {
            EastData data1 = result1.getResult();
            if(data1!= null) {
                if(data1.getData() != null) {
                    JSONArray jsonArray = (JSONArray)result1.getResult().getData();
                    String str = jsonArray.toJSONString();
                    List<StockNewHolderRecordDTO> ret = JSONObject.parseObject(str, new TypeReference<List<StockNewHolderRecordDTO>>() {});
                    return ret;
                }
            }
        }
        return null;
    }

    /**
     * 获取公司股股东细信息
     * @return
     */
    public StockHolderRecordListDTO getStockHodlerInfoByCode(String stockCode) {
        Map<String, String> params = new HashMap<>();
        if(stockCode.startsWith("300") || stockCode.startsWith("00")) {
            stockCode = stockCode+".SZ";
        } else if(stockCode.startsWith("6")) {
            stockCode = stockCode + ".SH";
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
        System.out.println(result);
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
        System.out.println(result);
        TycResult<List<FinanceAnalysisDataDTO>> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<List<FinanceAnalysisDataDTO>>>() {
        });
        if(eastMoneyResult == null) {
            return null;
        }
        return eastMoneyResult.getData();
    }

    /**
     * http://quote.eastmoney.com/center/boardlist.html#industry_board
     * @return
     */
    public List<BKInfoDTO> getBKList() {
        Map<String, String> params = new HashMap<>();
        params.put("pn", "1");
        params.put("pz", "5000");
        params.put("po", "1");
        params.put("np", "1");
        params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "f3");
        params.put("fs", "m:90+t:2+f:!50");
        params.put("fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f33,f11,f62,f128,f136,f115,f152,f124,f107,f104,f105,f140,f141,f207,f208,f209,f222");
        String result = HttpClientUtils.doGetForString(UrlConstant.BK_Info_URL,
                HeaderUtils.getEastBKWebHeaders(), params);
        TycResult<BKInfoListDTO> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<BKInfoListDTO>>() {
        });
        if(eastMoneyResult == null || eastMoneyResult.getData() == null) {
            return  null;
        }
        return eastMoneyResult.getData().getDiff();
    }

    public List<StockInfoDto> getStockListByBkCode(String bkCode) {
        Map<String, String> params = new HashMap<>();
        params.put("fid", "f62");
        params.put("po", "1");
        params.put("pz", "5000");
        params.put("pn", "1");
        params.put("np", "1");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("ut", "b2884a393a59ad64002292a3e90d46a5");
        params.put("fs", "b:"+bkCode);
        params.put("fields", "f12,f14,f2,f3,f62,f184,f66,f69,f72,f75,f78,f81,f84,f87,f204,f205,f124,f1,f13");
        String result = HttpClientUtils.doGetForString(UrlConstant.BK_Stock_Info_URL,
                HeaderUtils.getEastBKDetailWebHeaders(bkCode), params);
        TycResult<StockInfoListDto> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<StockInfoListDto>>() {
        });
        if(eastMoneyResult == null || eastMoneyResult.getData() == null) {
            return  null;
        }
        return eastMoneyResult.getData().getDiff();

    }



    /**
     * http://quote.eastmoney.com/center/boardlist.html#concept_board
     * @return
     */
    public List<BKInfoDTO> getConceptList() {
        Map<String, String> params = new HashMap<>();
        params.put("pn", "1");
        params.put("pz", "5000");
        params.put("po", "1");
        params.put("np", "1");
        params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "f3");
        params.put("fs", "m:90 t:3 f:!50");
        params.put("fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f33,f11,f62,f128,f136,f115,f152,f124,f107,f104,f105,f140,f141,f207,f208,f209,f222");
        String result = HttpClientUtils.doGetForString(UrlConstant.Concept_Info_URL,
                HeaderUtils.getEastConceptWebHeaders(), params);
        TycResult<BKInfoListDTO> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<BKInfoListDTO>>() {
        });
        if(eastMoneyResult == null || eastMoneyResult.getData() == null) {
            return  null;
        }
        return eastMoneyResult.getData().getDiff();
    }


    public List<StockInfoDto> getStockListByConceptCode(String conceptCode) {
        Map<String, String> params = new HashMap<>();
        params.put("fid", "f62");
        params.put("po", "1");
        params.put("pz", "5000");
        params.put("pn", "1");
        params.put("np", "1");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("ut", "b2884a393a59ad64002292a3e90d46a5");
        params.put("fs", "b:"+conceptCode);
        params.put("fields", "f12,f14,f2,f3,f62,f184,f66,f69,f72,f75,f78,f81,f84,f87,f204,f205,f124,f1,f13");
        String result = HttpClientUtils.doGetForString(UrlConstant.Concept_Stock_Info_URL,
                HeaderUtils.getEastBKDetailWebHeaders(conceptCode), params);
        TycResult<StockInfoListDto> eastMoneyResult = JSONObject.parseObject(result, new TypeReference<TycResult<StockInfoListDto>>() {
        });
        if(eastMoneyResult == null || eastMoneyResult.getData() == null) {
            return  null;
        }
        return eastMoneyResult.getData().getDiff();

    }



    public static void main(String[] args) {
        EastMoneyCrawler tycCrawler = new EastMoneyCrawler();
        List<StockInfoDto> list = tycCrawler.getStockList();
//        StockDetailDto detailDto = tycCrawler.getStockInfoByStockCode("300290");
//        List<BKInfoDTO> bkInfoDTOList = tycCrawler.getConceptList();
//        List<StockInfoDto> bkstock = tycCrawler.getStockListByConceptCode("BK1141");
//        List<FinanceReportTimeDTO> ret = tycCrawler.getFinanceReport("300716");

//        List<FinanceAnalysisDataDTO> data = tycCrawler.getFinanceAnalysisData("2021-03-31,2020-12-31,2020-09-30,2020-06-30,2020-03-31,2019-12-31","300716");
//        log.info("test");


        //tycCrawler.getHolderData("000952");
        tycCrawler.getStockNewHodlerInfoByCode("000952", "2016-06-30");

    }
}
