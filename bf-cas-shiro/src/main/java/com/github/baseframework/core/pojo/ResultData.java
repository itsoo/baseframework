package com.github.baseframework.core.pojo;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 方法返回结果
 *
 * @author zhouhy
 */
@Data
public class ResultData {

    public ResultData() {}

    public ResultData(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ResultData(boolean success, String message, String code, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 默认false
     */
    private boolean success = false;
    /**
     * 异常CODE
     */
    private String code;
    /**
     * 异常堆栈信息
     */
    private String stackTrace;
    /**
     * 返回消息
     */
    private String message;
    /**
     * 数据
     */
    private Object data;

    private Map result = new HashMap();

    public Map getResult() { return result; }

    public void setResult(Map result) { this.result.putAll(result); }

    public static ResultData getSuccess(String msg) {
        ResultData result = new ResultData();
        result.setMessage(msg);
        result.setSuccess(true);

        return result;
    }

    public static ResultData getFailure(String msg) {
        ResultData result = new ResultData();
        result.setMessage(msg);

        return result;
    }
}
