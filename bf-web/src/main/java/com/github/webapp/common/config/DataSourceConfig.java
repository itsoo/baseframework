package com.github.webapp.common.config;

import com.github.baseframework.pageplugin.PagePlugin;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * DataSourceConfig
 *
 * @author zxy
 */
@Configuration
public class DataSourceConfig {

    @Autowired
    private PagePlugin pagePlugin;

    @Bean
    public MybatisProperties mybatisProperties() { return new MybatisProperties(); }

    @Bean
    public JndiProperties jndiProperties() { return new JndiProperties(); }

    /**
     * JNDI 数据源
     *
     * @return DataSource
     */
    @Bean
    @Primary
    public DataSource dataSource(@Autowired JndiProperties jndiProperties) {
        return new JndiDataSourceLookup().getDataSource(jndiProperties.getJndiName());
    }

    /**
     * sqlSessionFactory
     *
     * @param dataSource        DataSource
     * @param mybatisProperties MybatisProperties
     * @return SqlSessionFactory
     * @throws Exception Exception
     */
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Autowired DataSource dataSource,
                                               @Autowired MybatisProperties mybatisProperties) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 配置插件属性
        Properties properties = new Properties();
        properties.putAll(mybatisProperties.getPlugin());
        pagePlugin.setProperties(properties);
        sqlSessionFactoryBean.setPlugins(pagePlugin);
        // 配置 Mapper 路径
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mybatisProperties.getMapperLocations()));
        // 其他配置信息
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean.getObject();
    }

    /**
     * sqlSessionTemplate
     *
     * @param sqlSessionFactory SqlSessionFactory
     * @return SqlSessionTemplate
     */
    @Bean
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Autowired SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * mybatis properties
     */
    @Data
    @Component
    @ConfigurationProperties(prefix = "mybatis")
    class MybatisProperties {

        private String typeAliasesPackage;
        private String mapperLocations;
        private Map<String, String> plugin = new HashMap<>();
    }

    /**
     * jndi properties
     */
    @Data
    @Component
    @ConfigurationProperties(prefix = "spring.datasource")
    class JndiProperties { private String jndiName; }
}
