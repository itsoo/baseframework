package com.github.webapp.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

/**
 * CommonUtils
 *
 * @author zxy
 */
@Slf4j
public class CommonUtils {

    public static void write(HttpServletResponse response, Map<String, Object> map) {
        write(response, GsonUtils.toJson(map));
    }

    public static void write(HttpServletResponse response, String outString) {
        write(response, "application/json;charset=UTF-8", outString);
    }

    public static void write(HttpServletResponse response, String contentType, String outString) {
        Objects.requireNonNull(contentType);

        response.setContentType(contentType);
        String[] split = contentType.split("=");

        try (OutputStream out = response.getOutputStream()) {
            out.write(outString.getBytes(split.length > 1 ? split[1] : "UTF-8"));
            out.flush();
        } catch (IOException e) {
            log.error("response write io ERROR!", e);
        }
    }
}
