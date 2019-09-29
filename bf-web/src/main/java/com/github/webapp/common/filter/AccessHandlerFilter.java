package com.github.webapp.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AccessHandlerFilter
 *
 * @author zxy
 */
@Slf4j
@Component
public class AccessHandlerFilter extends HandlerInterceptorAdapter {

    public static final String TOKEN_KEY = "token";

    /**
     * set token to response header
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    private void setTokenFromSession(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求头中的 token
        String token = request.getHeader(TOKEN_KEY);
        if (token != null) {
            response.setHeader(TOKEN_KEY, token);
            return;
        }

        // 获取 JSESSIONID 并添加至响应头
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (ShiroHttpSession.DEFAULT_SESSION_ID_NAME.equals(cookie.getName())) {
                response.setHeader(TOKEN_KEY, cookie.getValue());
                return;
            }
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (log.isDebugEnabled()) {
            log.debug("AccessHandlerFilter.class is invoked..");
        }

        setTokenFromSession(request, response);

        return true;
    }
}
