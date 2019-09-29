package com.github.baseframework.core.view;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
public class AuthorizationExceptionView {

    /**
     * 值
     */
    private Map msgMap;

    /**
     * 异步返回值
     */
    private AjaxResponseValue ajaxResponseValue;

    /**
     * 错误页路径
     */
    private String errorPagePath;
}
