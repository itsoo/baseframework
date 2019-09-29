package com.github.webapp.module.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * BaseController
 *
 * @author zxy
 */
public class BaseController {

    /**
     * 渲染首页
     *
     * @param response HttpServletResponse
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    protected void renderIndex(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        renderPage("index", request, response);
    }

    /**
     * 渲染目标页面
     *
     * @param page     String
     * @param response HttpServletResponse
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    protected void renderPage(String page, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getRequestDispatcher(getRealPath(page)).forward(request, response);
    }

    /**
     * 获取目标的真实相对路径
     *
     * @param pageName 目标页
     * @return 视图地址
     */
    private String getRealPath(String pageName) {
        Objects.requireNonNull(pageName);

        StringBuilder result = new StringBuilder().append("/view/").append(pageName);

        if (!pageName.endsWith(".html")) {
            result.append(".html");
        }

        return result.toString();
    }
}
