package com.github.webapp.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ApplicationConfig
 *
 * @author zxy
 */
@Data
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationConfig {

    private String env;
    private String title;
    private String description;
    private String version;
    private String basePackage;
    private String pathReg;
}
