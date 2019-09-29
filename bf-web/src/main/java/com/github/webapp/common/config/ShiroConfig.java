package com.github.webapp.common.config;

import com.github.baseframework.core.pojo.BaseResultBean;
import com.github.baseframework.core.realm.UserRealm;
import com.github.baseframework.core.view.AuthorizationExceptionView;
import com.github.webapp.common.filter.LoginFormFilter;
import lombok.Data;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ShiroConfig
 *
 * @author zxy
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroProperties shiroProperties() { return new ShiroProperties(); }

    @Bean
    public LoginFormFilter loginFilter() { return new LoginFormFilter(); }

    /**
     * shiroFilter
     *
     * @param securityManager     SecurityManager
     * @param shiroProperties     ShiroProperties
     * @param loginFilter         LoginFormFilter
     * @param errorPageProperties ErrorPageProperties
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Autowired SecurityManager securityManager,
                                              @Autowired ShiroProperties shiroProperties,
                                              @Autowired LoginFormFilter loginFilter,
                                              @Autowired ErrorPageProperties errorPageProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 配置登录回调地址
        shiroFilterFactoryBean.setLoginUrl(
                shiroProperties.getCasServer() + "/login?service=" + shiroProperties.getAppServer() +
                shiroProperties.getCasFilter());
        // 配置 shiro 拦截器
        Map<String, Filter> filters = new LinkedHashMap<>();
        // 配置自定义登录拦截器
        filters.put("authc", loginFilter);
        CasFilter casFilter = new CasFilter();
        // 配置验证错误时的失败页面
        casFilter.setFailureUrl(shiroProperties.getFailureUrl());
        // 配置验证成功时的页面
        casFilter.setSuccessUrl(shiroProperties.getSuccessUrl());
        filters.put("casFilter", casFilter);
        // 配置验证错误时的失败页面
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl(
                shiroProperties.getCasServer() + "/logout?service=" + shiroProperties.getAppServer());
        filters.put("logoutFilter", logoutFilter);
        shiroFilterFactoryBean.setFilters(filters);
        // 配置拦截规则
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put(shiroProperties.getCasFilter(), "casFilter");
        filterChainDefinitionMap.put("/logout", "logoutFilter");
        // 配置静态资源
        String[] srp = shiroProperties.getStaticResourcePath().split(",");
        String path;
        for (String str : srp) {
            if ((path = str.trim()).length() > 0) {
                filterChainDefinitionMap.put(path, "anon");
            }
        }
        // 无访问系统权限的错误页
        String errorPagePath = errorPageProperties.getErrorPagePath();
        filterChainDefinitionMap.put(errorPagePath + "/**", "anon");
        shiroFilterFactoryBean.setUnauthorizedUrl(errorPagePath + "/401.html");
        // everything else requires authentication
        filterChainDefinitionMap.put("/**", "authc");

        /*
         * anon:       表示可以匿名使用
         * authc:      表示需要认证才能使用
         * roles:      表示所限制的角色的列表
         * perms:      表示所限制的权限的列表
         * rest:       表示所限制的请求的方法
         * port:       表示所限制的端口的列表
         * authcBasic: 表示 httpBasic 认证
         * ssl:        表示安全的 url 请求 (协议为 https)
         * user:       表示必须存在用户
         */
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    /**
     * EhCache 缓存管理器
     *
     * @return EhCacheManager
     */
    @Bean
    public EhCacheManager cacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:config/shiro-ehcache.xml");

        return ehCacheManager;
    }

    /**
     * casRealm
     *
     * @param shiroProperties ShiroProperties
     * @return UserRealm
     */
    @Bean
    public UserRealm casRealm(@Autowired ShiroProperties shiroProperties) {
        UserRealm userRealm = new UserRealm();
        // 认证通过后的默认角色
        userRealm.setDefaultRoles("ROLE_USER");
        // cas 服务端地址前缀
        userRealm.setCasServerUrlPrefix(shiroProperties.getCasServer());
        userRealm.setCachingEnabled(true);
        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthenticationCacheName("authenticationCache");
        userRealm.setAuthorizationCachingEnabled(true);
        userRealm.setAuthorizationCacheName("authorizationCache");
        // 应用服务地址，用来接收 cas 服务端票据
        userRealm.setCasService(shiroProperties.getAppServer() + shiroProperties.getCasFilter());
        userRealm.setUrl(shiroProperties.getSecurityServer());
        userRealm.setProjectCode(shiroProperties.getProjectCode());

        return userRealm;
    }

    /**
     * Shiro's main business-tier object for web-enabled applications
     *
     * @param casRealm     UserRealm
     * @param cacheManager EhCacheManager
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(@Autowired UserRealm casRealm, @Autowired EhCacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置 Realm
        securityManager.setRealm(casRealm);
        // 配置缓存
        securityManager.setCacheManager(cacheManager);

        return securityManager;
    }

    /**
     * authorizationAttributeSourceAdvisor
     *
     * @param securityManager SecurityManager
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Autowired SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);

        return advisor;
    }

    /**
     * lifecycleBeanPostProcessor
     *
     * @return LifecycleBeanPostProcessor
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() { return new LifecycleBeanPostProcessor(); }

    /**
     * methodInvokingFactoryBean
     *
     * @param securityManager SecurityManager
     * @return MethodInvokingFactoryBean
     */
    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(@Autowired SecurityManager securityManager) {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(securityManager);

        return methodInvokingFactoryBean;
    }

    /**
     * 权限不足跳转页面配置
     *
     * @param errorPageProperties ErrorPageProperties
     * @param baseResultBean      BaseResultBean
     * @return AuthorizationExceptionView
     */
    @Bean
    public AuthorizationExceptionView authorizationExceptionView(@Autowired ErrorPageProperties errorPageProperties,
                                                                 @Autowired BaseResultBean baseResultBean) {
        AuthorizationExceptionView authorizationExceptionView = new AuthorizationExceptionView();
        // 配置错误页面
        String errorPagePath = errorPageProperties.getErrorPagePath();
        authorizationExceptionView.setAjaxResponseValue(baseResultBean);
        authorizationExceptionView.setErrorPagePath(errorPagePath);

        return authorizationExceptionView;
    }

    /**
     * 权限验证结果
     *
     * @return BaseResultBean
     */
    @Bean
    public BaseResultBean baseResultBean() { return new BaseResultBean(); }

    /**
     * shiroProperties
     */
    @Data
    @Component
    @ConfigurationProperties(prefix = "shiro")
    public class ShiroProperties {

        private String appServer;
        private String casServer;
        private String securityServer;
        private String failureUrl;
        private String successUrl;
        private String projectCode;
        private String casFilter;
        private String staticResourcePath;
    }

    /**
     * 错误页配置容器
     */
    @Data
    @Component
    @ConfigurationProperties(prefix = "errors")
    public class ErrorPageProperties { private String errorPagePath; }
}
