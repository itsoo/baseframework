package com.github.webapp.common.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;

/**
 * Filter 配置
 *
 * @author zxy
 */
@Configuration
public class FilterConfig {

    @Autowired
    private ShiroConfig.ShiroProperties shiroProperties;

    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("singleSignOutFilter");
        registrationBean.setFilter(new SingleSignOutFilter());
        registrationBean.addInitParameter("casServerUrlPrefix", shiroProperties.getCasServer());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean shiroFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("shiroFilter");
        registrationBean.setFilter(new DelegatingFilterProxy());
        registrationBean.addInitParameter("targetFilterLifecycle", "true");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        registrationBean.setDispatcherTypes(DispatcherType.FORWARD);
        registrationBean.setDispatcherTypes(DispatcherType.INCLUDE);

        return registrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean listableBeanFactory() {
        ServletListenerRegistrationBean listableBeanFactory = new ServletListenerRegistrationBean();
        listableBeanFactory.setListener(new SingleSignOutHttpSessionListener());

        return listableBeanFactory;
    }
}
