package com.github.webapp.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.baseframework.core.pojo.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常处理基类
 *
 * @author zxy
 */
@Slf4j
public class BaseGlobalExceptionHandler {

    ModelAndView handleError(HttpServletResponse res, Exception e, String code, HttpStatus status) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return handleAjaxError(res, code, getStackTrace(e), status);
    }

    private ModelAndView handleAjaxError(HttpServletResponse res, String code, String stackTrace, HttpStatus status) {
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(status.value());

        ResultData resultData = new ResultData();
        resultData.setMessage("System Error");
        resultData.setCode(code);
        resultData.setStackTrace(stackTrace);

        try (PrintWriter writer = res.getWriter()) {
            writer.write(JSONObject.toJSONString(resultData));
            writer.flush();
        } catch (IOException e) {
            log.error("Response ajax message view", e);
        }

        return null;
    }

    private String getStackTrace(Throwable throwable) {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);

            return sw.toString();
        } catch (Exception e) {
            log.error("get stack trace is error", e);
        }

        return null;
    }
}
