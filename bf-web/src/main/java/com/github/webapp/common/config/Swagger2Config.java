package com.github.webapp.common.config;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2Config
 *
 * @author zxy
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Autowired
    private ApplicationConfig applicationConfig;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(apis()).paths(paths()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(applicationConfig.getTitle())
                                   .description(applicationConfig.getDescription())
                                   .version(applicationConfig.getVersion())
                                   .build();
    }

    private Predicate<RequestHandler> apis() {
        return RequestHandlerSelectors.basePackage(applicationConfig.getBasePackage());
    }

    private Predicate<String> paths() {
        return PathSelectors.regex(applicationConfig.getPathReg());
    }
}
