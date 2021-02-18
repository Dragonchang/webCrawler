package com.dragonchang.crawler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dragonchang.constant.UrlConstant;
import com.dragonchang.domain.dto.tyc.NameDto;
import com.dragonchang.domain.dto.tyc.ShareCompanyDto;
import com.dragonchang.domain.dto.tyc.ShareCompanyListDto;
import com.dragonchang.domain.vo.TycResult;
import com.dragonchang.tianyancha.HeaderUtils;
import com.dragonchang.tianyancha.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-17 13:08
 **/
@Slf4j
@Component
public class TycCrawler {

    private static String defaultList[] = {
            "6, b, t, f, 2, z, l, 5, w, h, q, i, s, e, c, p, m, u, 9, 8, y, k, j, r, x, n, -, 0, 3, 4, d, 1, a, o, 7, v, g",
            "1, 8, o, s, z, u, n, v, m, b, 9, f, d, 7, h, c, p, y, 2, 0, 3, j, -, i, l, k, t, q, 4, 6, r, a, w, 5, e, x, g",
            "s, 6, h, 0, p, g, 3, n, m, y, l, d, x, e, a, k, z, u, f, 4, r, b, -, 7, o, c, i, 8, v, 2, 1, 9, q, w, t, j, 5",
            "x, 7, 0, d, i, g, a, c, t, h, u, p, f, 6, v, e, q, 4, b, 5, k, w, 9, s, -, j, l, y, 3, o, n, z, m, 2, 1, r, 8",
            "z, j, 3, l, 1, u, s, 4, 5, g, c, h, 7, o, t, 2, k, a, -, e, x, y, b, n, 8, i, 6, q, p, 0, d, r, v, m, w, f, 9",
            "j, h, p, x, 3, d, 6, 5, 8, k, t, l, z, b, 4, n, r, v, y, m, g, a, 0, 1, c, 9, -, 2, 7, q, e, w, u, s, f, o, i",
            "8, q, -, u, d, k, 7, t, z, 4, x, f, v, w, p, 2, e, 9, o, m, 5, g, 1, j, i, n, 6, 3, r, l, b, h, y, c, a, s, 0",
            "d, 4, 9, m, o, i, 5, k, q, n, c, s, 6, b, j, y, x, l, a, v, 3, t, u, h, -, r, z, 2, 0, 7, g, p, 8, f, 1, w, e",
            "7, -, g, x, 6, 5, n, u, q, z, w, t, m, 0, h, o, y, p, i, f, k, s, 9, l, r, 1, 2, v, 4, e, 8, c, b, a, d, j, 3",
            "1, t, 8, z, o, f, l, 5, 2, y, q, 9, p, g, r, x, e, s, d, 4, n, b, u, a, m, c, h, j, 3, v, i, 0, -, w, 7, k, 6"
    };


    /**
     * 获取指定公司的股权穿透持有公司股份信息
     * @param tycCompanyID
     * @return
     */
    public ShareCompanyListDto getShareCompanyInfo(String tycCompanyID) {
        NameDto dto = getNameResult(tycCompanyID);
        String cloud_token = getCloudToken(dto);
        System.out.println(cloud_token);
        String cloud_utm = getCloudUtm(tycCompanyID, dto);
        Map<String, String> params = new HashMap<>();
        params.put("id", tycCompanyID);
        String result = HttpClientUtils.doGetForString(UrlConstant.Share_Node_URL,
                HeaderUtils.getTYCWebIndexNodeHeaders(cloud_token, cloud_utm), params);
        System.out.println(result);
        TycResult<ShareCompanyListDto> tycResult = JSONObject.parseObject(result, new TypeReference<TycResult<ShareCompanyListDto>>() {
        });
        return tycResult.getData();
    }

    /**
     * 股权穿透 name.json
     *
     * @return
     */
    public NameDto getNameResult(String companyID) {
        Map<String, String> params = new HashMap<>();
        params.put("id", companyID);
        String currentTime = String.valueOf(System.currentTimeMillis());
        params.put("random", currentTime);
        String result = HttpClientUtils.doGetForString(UrlConstant.Share_Name_URL, HeaderUtils.getTYCWebNameHeaders(), params);
        System.out.println(result);
        TycResult<NameDto> tycResult = JSONObject.parseObject(result, new TypeReference<TycResult<NameDto>>() {
        });
        NameDto dto = null;
        if (TycResult.SUCCESS.equals(tycResult.getState())) {
            dto = tycResult.getData();
        } else {
            log.error("get name failed!");
            return null;
        }
        return dto;
    }

    /**
     * 获取indexnode请求Cookie中的CloudToken
     *
     * @param dto
     * @return
     */
    public String getCloudToken(NameDto dto) {
        String voV = getNameV(dto);
        System.out.println("voV: " + voV);
        if(!StringUtils.isEmpty(voV)) {
            int beginIndex = voV.indexOf("cloud_token=");
            int endIndex = voV.indexOf(";path=");
            return voV.substring(beginIndex, endIndex);
        }
        return null;
    }

    /**
     * 获取indexnode请求Cookie中的CloudUtm
     *
     * @param dto
     * @return
     */
    public String getCloudUtm(String companyId, NameDto dto) {
        if (dto == null) {
            return null;
        }
        String voV = getNameV(dto);
        System.out.println("voV: " + voV);
        if(!StringUtils.isEmpty(voV)) {
            int beginIndex = voV.indexOf("{return'");
            beginIndex = beginIndex + 8;
            int endIndex = voV.indexOf("'}}(window)");
            String wtfReturn = voV.substring(beginIndex, endIndex);
            String windowSogo = getSogo(companyId);
            if(!StringUtils.isEmpty(wtfReturn)) {
                String[] stringArr = wtfReturn.split(",");
                String[] retArr = windowSogo.split(",");
                System.out.println("stringArr: " + stringArr.length);
                System.out.println("retArr: " + retArr.length);
                StringBuilder stringBuilder = new StringBuilder();
                for (String str : stringArr) {
                    int index = Integer.valueOf(str).intValue();
                    stringBuilder.append(retArr[index]);
                }
                if(stringBuilder.length() >0 ) {
                    return stringBuilder.toString().replace(" ", "");
                }
            }
        }
        return "8b064ea704b34f5ca3356dfa365c4b4d";
    }

    /**
     *
     * @param dto
     * @return
     */
    private String getNameV(NameDto dto) {
        if (dto == null) {
            return null;
        }
        String strV = dto.getV();
        System.out.println("V: " + strV);
        if (!StringUtils.isEmpty(strV)) {
            String[] stringArr = strV.split(",");
            System.out.println("stringArr: " + stringArr.length);
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : stringArr) {
                int value = Integer.parseInt(str);
                char c = (char) value;
                stringBuilder.append(c);
            }
            if(stringBuilder.length() >0 ) {
                return stringBuilder.toString();
            }
        }
        return null;
    }

    /**
     *
     * @param companyID
     * @return
     */
    private String getSogo(String companyID) {
        String id = companyID.substring(0, 1);
        Integer idAscii = Integer.valueOf(id.charAt(0)).intValue();
        String ascii = idAscii.toString();
        String strIndex = ascii.substring(1,2);
        int index = Integer.valueOf(strIndex).intValue();
        return defaultList[index];
    }

    public static void main(String[] args) {
        String companyID = "3484794127";
        TycCrawler tycCrawler = new TycCrawler();
        ShareCompanyListDto list = tycCrawler.getShareCompanyInfo(companyID);
        System.out.println(companyID);
//        NameDto dto = tycCrawler.getNameResult(companyID);
//        String cloud_token = tycCrawler.getCloudToken(dto);
//        System.out.println(cloud_token);
//        String cloud_utm = tycCrawler.getCloudUtm(companyID, dto);
//        Map<String, String> params = new HashMap<>();
//        params.put("id", companyID);
//        String result = HttpClientUtils.doGetForString(UrlConstant.Share_Node_URL,
//                HeaderUtils.getTYCWebIndexNodeHeaders(cloud_token, cloud_utm), params);
//        System.out.println(result);
    }
}
