package com.github.baseframework.core.pojo;

import com.alibaba.fastjson.JSONObject;
import com.github.baseframework.core.view.AjaxResponseValue;
import lombok.Data;

/**
 * 鉴权结果
 * > 如需扩展请 extends this
 * > 扩展 Bean 请加入到 web 工程中
 *
 * @author xuhuan
 */
@Data
public class BaseResultBean implements AjaxResponseValue {

    private boolean success;
    private String errorCode;
    private String errorMsg;

    @Override
    public String result() {
        this.success = false;
        this.errorCode = "401";
        this.errorMsg = "您的访问权限不足";
        return JSONObject.toJSONString(this);
    }
}
