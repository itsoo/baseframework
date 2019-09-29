package com.github.webapp.common.filter;

import com.github.webapp.common.config.ApplicationConfig;
import com.github.webapp.common.session.CacheSessionContext;
import com.github.webapp.constant.Consts;
import com.github.webapp.util.CommonUtils;
import com.github.webapp.util.HttpRequestUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LoginFormFilter
 *
 * @author zxy
 */
public class LoginFormFilter extends FormAuthenticationFilter {

    private static final CacheSessionContext CACHE = CacheSessionContext.getInstance();

    @Autowired
    private ApplicationConfig applicationConfig;

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (HttpRequestUtils.isAjax(httpRequest)) {
            if (!Consts.Env.PROD.equals(applicationConfig.getEnv())) {
                setHeader(httpRequest, httpResponse);
            }
        }

        return isLogged(httpRequest) || isAccessAllowed(httpRequest, httpResponse, mappedValue) ||
               onAccessDenied(httpRequest, httpResponse, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (isLoginRequest(httpRequest, httpResponse)) {
            if (isLoginSubmission(httpRequest, httpResponse)) {
                return executeLogin(httpRequest, httpResponse);
            }

            return true;
        }

        if (HttpRequestUtils.isAjax(httpRequest)) {
            sendSessionTimeout(httpResponse);
        } else {
            saveRequestAndRedirectToLogin(httpRequest, httpResponse);
        }

        return false;
    }

    private boolean isLogged(HttpServletRequest httpRequest) {
        return CACHE.getSession(httpRequest.getHeader(AccessHandlerFilter.TOKEN_KEY)) != null;
    }

    private void setHeader(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // 设置允许跨域请求
        httpResponse.setHeader("Access-control-Allow-Origin", httpRequest.getHeader("Origin"));
        httpResponse.setHeader("Access-Control-Allow-Methods", httpRequest.getMethod());
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Headers", httpRequest.getHeader("Access-Control-Request-Headers"));
        httpResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpResponse.setStatus(HttpServletResponse.SC_OK);
    }

    private void sendSessionTimeout(HttpServletResponse httpResponse) {
        // Status code (403) 定义为登录超时
        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        CommonUtils.write(httpResponse, Consts.NoLoggedResponse.getMessage());
    }
}
