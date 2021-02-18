package com.dragonchang.domain.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class JsonResult<T> {
    public static final String SUCCESS="0";
    public static final String FAIL="1";
    public static final String EXCEPTION="2";

    private String code;

    private String message;

    private T data;

    public static <T> JsonResult<T> success(T data) {
        JsonResult<T> resp = new JsonResult<T>();
        resp.setCode(SUCCESS);
        resp.setData(data);
        return resp;
    }

    public static <T> JsonResult<T> success() {
        JsonResult<T> resp = new JsonResult<T>();
        resp.setCode(SUCCESS);
        return resp;
    }

    public static <T> JsonResult<T> failure() {
        JsonResult<T> resp = new JsonResult<T>();
        resp.setCode(FAIL);
        return resp;
    }

    public static <T> JsonResult<T> failure(String message) {
        JsonResult<T> resp = new JsonResult<T>();
        resp.setCode(FAIL);
        resp.setMessage(message);
        return resp;
    }

    public static <T> JsonResult<T> failure(String code, String message) {
        JsonResult<T> resp = new JsonResult<T>();
        resp.setCode(code);
        resp.setMessage(message);
        return resp;
    }

    public static <T> JsonResult<T> exception(String message) {
        JsonResult<T> resp = new JsonResult<T>();
        resp.setCode(EXCEPTION);
        resp.setMessage(message);
        return resp;
    }

    public boolean isSuccess() {
        return StringUtils.equals(this.code, SUCCESS);
    }
}
