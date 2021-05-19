package com.dragonchang.domain.enums;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-05-19 14:36
 **/
public enum FinanceReportTypeEnum {
    firstQuarter ("1", "一季报"),
    secondQuarter("2", "中报"),
    thirdQuarter("3", "三季报"),
    annualQuarter("4", "年报");

    private static final Map<String, String> VALUES_MAP;

    static {
        Map<String, String> map = new LinkedHashMap<>();
        for (FinanceReportTypeEnum each : values()) {
            map.put(each.getCode(), each.getName());
        }
        VALUES_MAP = Collections.unmodifiableMap(map);
    }

    private String code;
    private String name;

    FinanceReportTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Map<String, String> toMap() {
        return VALUES_MAP;
    }

    public static FinanceReportTypeEnum getEnumByCode(String code) {
        for (FinanceReportTypeEnum each : values()) {
            if (each.getCode().equals(code)) {
                return each;
            }
        }
        return null;
    }

    public static String getNameByCode(String code) {
        FinanceReportTypeEnum en = getEnumByCode(code);
        if (null != en) {
            return en.getName();
        }
        return null;
    }
}
