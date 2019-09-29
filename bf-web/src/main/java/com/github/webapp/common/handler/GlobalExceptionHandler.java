package com.github.webapp.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 *
 * @author zxy
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends BaseGlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundError(HttpServletResponse res, Exception e) {
        log.error("++ 全局 404 异常捕获 ++", e);

        try {
            return handleError(res, e, "404", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("GlobalExceptionHandler::handleNotFoundError => ", ex);

            return null;
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleInternalServerError(HttpServletResponse res, Exception e) {
        log.error("++ 全局 500 异常捕获 ++", e);

        try {
            if (e instanceof AuthorizationException) {
                return handleError(res, e, "401", HttpStatus.BAD_REQUEST);
            }

            return handleError(res, e, "500", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            log.error("GlobalExceptionHandler::handleInternalServerError => ", ex);

            return null;
        }
    }
}
