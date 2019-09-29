package com.github.webapp.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统常量
 *
 * @author zxy
 */
public class Consts {

    /**
     * 运行时环境
     */
    public interface Env {
        String DEV = "dev";
        String TEST = "test";
        String PROD = "prod";
    }

    /**
     * 未登录错误响应
     */
    public interface NoLoggedResponse {

        /**
         * 获取错误信息
         *
         * @return Map
         */
        static Map<String, Object> getMessage() {
            Map<String, Object> map = new HashMap<>(8);
            map.put("status", 403);
            map.put("errorCode", "timeout");

            return map;
        }
    }
}
