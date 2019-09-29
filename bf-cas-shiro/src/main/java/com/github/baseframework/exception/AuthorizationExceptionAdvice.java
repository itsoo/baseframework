package com.github.baseframework.exception;

import com.github.baseframework.core.view.AuthorizationExceptionView;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@ControllerAdvice
public class AuthorizationExceptionAdvice {

    @Autowired
    private AuthorizationExceptionView authorizationExceptionView;

    /**
     * 判断 ajax 请求
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    @ExceptionHandler(AuthorizationException.class)
    public void handleAuthorizationException(HttpServletRequest request,
                                             HttpServletResponse response,
                                             AuthorizationException e) throws ServletException, IOException {
        if (isAjax(request)) {
            // ajax 请求
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");

            try (PrintWriter out = response.getWriter()) {
                out.write(authorizationExceptionView.getAjaxResponseValue().result());
                out.flush();
            } catch (Exception ex) {
                log.error("用户身份认证异常", e);
            }
        }

        // 非 ajax 返回错误页面
        request.getRequestDispatcher(authorizationExceptionView.getErrorPagePath() + "/401.html")
               .forward(request, response);
    }
}
