package com.github.webapp.util;

import com.github.baseframework.core.pojo.User;
import com.github.baseframework.util.ShiroUtils;
import com.github.webapp.common.filter.AccessHandlerFilter;
import com.github.webapp.common.session.CacheSessionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * HttpUtils
 *
 * @author zxy
 */
public class HttpRequestUtils {

    /**
     * 判断是否为 ajax 请求
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isAjax(HttpServletRequest request) {
        String accept = request.getHeader("accept");

        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ||
               (accept != null && accept.contains("application/json")) ||
               request.getHeader(AccessHandlerFilter.TOKEN_KEY) != null;
    }

    /**
     * 获取用户
     *
     * @param request HttpServletRequest
     * @return User
     */
    public static User getCurrUser(HttpServletRequest request) {
        User user = ShiroUtils.getUserEntity();
        if (user == null) {
            String token = request.getHeader(AccessHandlerFilter.TOKEN_KEY);

            return getCurrUser(CacheSessionContext.getInstance().getSession(token));
        }

        return user;
    }

    private static User getCurrUser(HttpSession session) {
        if (session == null) {
            return null;
        }

        return (User) session.getAttribute("user");
    }
}
