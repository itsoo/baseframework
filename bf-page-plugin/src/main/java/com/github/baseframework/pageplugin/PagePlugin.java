package com.github.baseframework.pageplugin;

import com.github.baseframework.pageplugin.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import javax.xml.bind.PropertyException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 分页插件
 *
 * @author zxy
 */
@Slf4j
@Component
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}))
@SuppressWarnings("unused")
public class PagePlugin implements Interceptor {

    /**
     * 数据库方言
     */
    private static String dialect;

    /**
     * 需要拦截的 Mapper
     */
    private static String regexp;

    /**
     * 分页拦截方法
     *
     * @param ivk Invocation
     * @return Object
     * @throws Throwable Throwable
     */
    @Override
    public Object intercept(Invocation ivk) throws Throwable {
        if (ivk.getTarget() instanceof StatementHandler) {
            StatementHandler handler = (StatementHandler) ivk.getTarget();
            MetaObject metaHandler = SystemMetaObject.forObject(handler);
            MappedStatement stmt = (MappedStatement) metaHandler.getValue("delegate.mappedStatement");

            if (stmt.getId().matches(regexp)) {
                BoundSql boundSql = (BoundSql) metaHandler.getValue("delegate.boundSql");

                Object paramObject = boundSql.getParameterObject();
                Object object = PageUtil.getValueOrDefault(paramObject, PageInfo.getInstance());

                if (!PageInfo.isFalsePage(object)) {
                    Connection conn = (Connection) ivk.getArgs()[0];
                    String sql = boundSql.getSql();
                    int count = PageUtil.queryTotalCount(sql, conn, stmt, boundSql, object);
                    Map<String, Object> pageInfo = initPageInfo(object, count);

                    // 处理分页查询前置条件
                    int pageSize = (int) pageInfo.getOrDefault(PageInfo.PAGE_SIZE, PageInfo.defaultPageSize);
                    Map<String, Object> condition = count <= pageSize ? null : pageInfo;

                    String pageSql = PageUtil.generatePageSql(dialect, sql, condition);

                    ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql);
                }
            }
        }

        return ivk.proceed();
    }

    /**
     * 注入插件
     *
     * @param arg0 Object
     * @return Object
     */
    @Override
    public Object plugin(Object arg0) { return Plugin.wrap(arg0, this); }

    /**
     * 注入属性
     *
     * @param p Properties
     */
    @Override
    public void setProperties(Properties p) {
        dialect = p.getProperty(PageInfo.DIALECT);
        if (PageUtil.isEmpty(dialect)) {
            log.error("必须的属性注入异常", new PropertyException("dialect 属性用于指定数据库方言"));
        }

        regexp = p.getProperty(PageInfo.REGEXP);
        if (PageUtil.isEmpty(regexp)) {
            log.error("必须的属性注入异常", new PropertyException("regexp 属性用于指定 SQL 拦截表达式"));
        }

        // 设置 pageSize 初始值
        String pageSize = p.getProperty(PageInfo.PAGE_SIZE);
        if (!PageUtil.isEmpty(pageSize)) {
            PageInfo.defaultPageSize = Integer.parseInt(pageSize);
        }
    }

    /**
     * 初始化 PageInfo
     *
     * @param obj   Object
     * @param count int
     * @return Map
     * @throws Throwable Throwable
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> initPageInfo(Object obj, int count) throws Throwable {
        // 参数为 Map
        if (obj instanceof Map) {
            Map<String, Object> param = (Map) obj;
            Map<String, Object> map = new HashMap<>(param);

            Map<String, Object> pageInfo = (Map) map.get(PageInfo.PAGE);
            if (pageInfo == null) {
                map.put(PageInfo.TOTAL_COUNT, count);
                pageInfo = PageInfo.getInstance(map);
            }

            param.put(PageInfo.PAGE, pageInfo);

            return pageInfo;
        }

        // 参数为其它 Bean
        Field pageField = ReflectHelper.getFieldByFieldName(obj, PageInfo.PAGE);
        if (pageField != null) {
            Map<String, Object> pageInfo = (Map) ReflectHelper.getValueByFieldName(obj, PageInfo.PAGE);
            pageInfo = PageUtil.getValueOrDefault(pageInfo, new HashMap(16));
            pageInfo.put(PageInfo.TOTAL_COUNT, count);
            PageInfo.getInstance(pageInfo);

            // 反射分页对象
            ReflectHelper.setValueByFieldName(obj, PageInfo.PAGE, pageInfo);

            return pageInfo;
        }

        throw new NoSuchFieldException(obj.getClass().getName());
    }
}
