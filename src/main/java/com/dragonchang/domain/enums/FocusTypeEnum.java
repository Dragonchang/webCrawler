package com.dragonchang.domain.enums;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public enum FocusTypeEnum {
    STOCK("1", "股份公司"),
    ORGAN("2", "机构"),
    PERSON("3", "个人");

    private static final Map<String, String> VALUES_MAP;

    static {
        Map<String, String> map = new LinkedHashMap<>();
        for (FocusTypeEnum each : values()) {
            map.put(each.getCode(), each.getName());
        }
        VALUES_MAP = Collections.unmodifiableMap(map);
    }

    private String code;
    private String name;

    FocusTypeEnum(String code, String name) {
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

    public static FocusTypeEnum getEnumByCode(String code) {
        for (FocusTypeEnum each : values()) {
            if (each.getCode().equals(code)) {
                return each;
            }
        }
        return null;
    }

    public static String getNameByCode(String code) {
        FocusTypeEnum en = getEnumByCode(code);
        if (null != en) {
            return en.getName();
        }
        return null;
    }
}
