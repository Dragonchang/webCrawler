package com.dragonchang.domain.enums;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 63474
 */

public enum HolderTypeEnum {
    GD("1", "股东"),
    LT("2", "流通股东");

    private static final Map<String, String> VALUES_MAP;

    static {
        Map<String, String> map = new LinkedHashMap<>();
        for (HolderTypeEnum each : values()) {
            map.put(each.getCode(), each.getName());
        }
        VALUES_MAP = Collections.unmodifiableMap(map);
    }

    private String code;
    private String name;

    HolderTypeEnum(String code, String name) {
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

    public static HolderTypeEnum getEnumByCode(String code) {
        for (HolderTypeEnum each : values()) {
            if (each.getCode().equals(code)) {
                return each;
            }
        }
        return null;
    }

    public static String getNameByCode(String code) {
        HolderTypeEnum en = getEnumByCode(code);
        if (null != en) {
            return en.getName();
        }
        return null;
    }
}
