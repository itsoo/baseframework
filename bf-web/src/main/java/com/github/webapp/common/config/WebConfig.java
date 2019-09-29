package com.github.webapp.common.config;

import com.github.webapp.common.filter.AccessHandlerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig
 *
 * @author zxy
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AccessHandlerFilter accessHandlerFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessHandlerFilter);
    }
}
