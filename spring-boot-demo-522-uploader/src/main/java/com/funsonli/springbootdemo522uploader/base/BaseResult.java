package com.funsonli.springbootdemo522uploader.base;

import com.alibaba.fastjson.JSONObject;

public class BaseResult extends JSONObject {
    public static final Integer CODE_SUCCESS = 200;
    public static final Integer CODE_FAILED = 500;

    public static final String MSG_SUCCESS = "操作成功";
    public static final String MSG_FAILED = "操作失败";

    public static BaseResult ret(int status, String message, Object data) {
        BaseResult result = new BaseResult();
        result.put("status", status);
        result.put("code", status);
        result.put("message", message);
        result.put("data", data);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    public static BaseResult success(Object data) {
        return ret(BaseResult.CODE_SUCCESS, BaseResult.MSG_SUCCESS, data);
    }

    public static BaseResult success(String message) {
        return ret(BaseResult.CODE_SUCCESS, message, null);
    }

    public static BaseResult success(String message, Object data) {
        return ret(BaseResult.CODE_SUCCESS, message, data);
    }

    public static BaseResult success() {
        return success(MSG_SUCCESS);
    }

    public static BaseResult error(int status, String message) {
        return ret(status, message, null);
    }

    public static BaseResult error(int status) {
        return error(status, BaseResult.MSG_FAILED);
    }

    public static BaseResult error(String message) {
        return error(BaseResult.CODE_FAILED, message);
    }

    public static BaseResult error() {
        return error(BaseResult.CODE_FAILED, BaseResult.MSG_FAILED);
    }
}

