package com.github.webapp;

import com.github.webapp.common.config.ShiroConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Application
 *
 * @author zxy
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.github")
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer implements ErrorPageRegistrar {

    /**
     * ErrorPageProperties
     */
    @Autowired
    private ShiroConfig.ErrorPageProperties errorPageProperties;

    /**
     * Maven 打包入口
     *
     * @param args String[]
     */
    public static void main(String[] args) { SpringApplication.run(Application.class, args); }

    /**
     * 应用启动入口
     *
     * @param builder SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.bannerMode(Banner.Mode.OFF);
        return builder.sources(this.getClass());
    }

    /**
     * 定义错误页
     *
     * @param errorPageRegistry ErrorPageRegistry
     */
    @Override
    public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
        String path = errorPageProperties.getErrorPagePath() + "/index.html";
        // 捕获异常统一转发到 index.html 通过前端路由控制逻辑视图
        errorPageRegistry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, path),
                                        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, path));
    }
}
