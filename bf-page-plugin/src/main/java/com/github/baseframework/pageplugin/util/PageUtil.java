package com.github.baseframework.pageplugin.util;

import com.github.baseframework.pageplugin.PageInfo;
import com.github.baseframework.pageplugin.ReflectHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import javax.xml.bind.PropertyException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 *
 * @author zxy
 */
@Slf4j
@Component
public final class PageUtil {

    /**
     * 查询总页数
     *
     * @param sql        String
     * @param connection Connection
     * @param statement  MappedStatement
     * @param boundSql   BoundSql
     * @param obj        Object
     * @return int
     * @throws Throwable Throwable
     */
    public static int queryTotalCount(String sql, Connection connection, MappedStatement statement,
                                      BoundSql boundSql, Object obj) throws Throwable {
        // 查询总记录数
        String countSql = String.format("SELECT COUNT(1) FROM (%s) TMP_COUNT", sql);
        PreparedStatement countStmt = connection.prepareStatement(countSql);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        BoundSql countBs = new BoundSql(statement.getConfiguration(), countSql, parameterMappings, obj);

        // 复制元数据的参数
        Field metaParamsField = ReflectHelper.getFieldByFieldName(boundSql, "metaParameters");
        if (metaParamsField != null) {
            ReflectHelper.setValueByFieldName(
                    countBs, "metaParameters", ReflectHelper.getValueByFieldName(boundSql, "metaParameters"));
        }

        // 复制动态 SQL 的参数
        Field additionalField = ReflectHelper.getFieldByFieldName(boundSql, "additionalParameters");
        if (additionalField != null) {
            ReflectHelper.setValueByFieldName(
                    countBs, "additionalParameters", ReflectHelper.getValueByFieldName(boundSql, "additionalParameters"));
        }

        // 设置 SQL 参数
        setParameters(countStmt, statement, countBs, obj);

        try (ResultSet rs = countStmt.executeQuery()) {
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }

            return count;
        } finally {
            countStmt.close();
        }
    }

    /**
     * 根据数据库方言组织分页 SQL
     *
     * @param dialect  String
     * @param sql      String
     * @param pageInfo Map
     * @return String
     */
    public static String generatePageSql(String dialect, String sql, Map<String, Object> pageInfo) {
        if (pageInfo != null) {
            int startRows = PageInfo.getStartRows(pageInfo);

            if (PageUtil.isEquals(PageInfo.Dialect.MYSQL, dialect)) {
                return String.format(
                        "%s LIMIT %d, %d", sql, startRows, PageUtil.getNumber(pageInfo.get(PageInfo.PAGE_SIZE)));
            } else if (PageUtil.isEquals(PageInfo.Dialect.ORACLE, dialect)) {
                int endRows = PageInfo.getEndRows(pageInfo);
                String pageSql =
                        "SELECT * FROM (SELECT TMP_TB.*, ROWNUM ROW_NUM FROM (%s) TMP_TB) WHERE ROW_NUM > %d AND ROW_NUM <= %d";

                return String.format(pageSql, sql, startRows, endRows);
            }

            log.error("组织分页SQL异常", new PropertyException("未被分页组件支持的数据库：" + dialect));

            return sql;
        }

        return sql;
    }

    /**
     * 设置 SQL 参数
     *
     * @param ps        PreparedStatement
     * @param statement MappedStatement
     * @param boundSql  BoundSql
     * @param obj       Object
     * @throws SQLException SQLException
     */
    @SuppressWarnings("unchecked")
    private static void setParameters(PreparedStatement ps, MappedStatement statement, BoundSql boundSql, Object obj)
            throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(statement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        if (parameterMappings != null) {
            Configuration configuration = statement.getConfiguration();
            TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = obj == null ? null : configuration.newMetaObject(obj);

            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);

                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);

                    if (obj == null) {
                        value = null;
                    } else if (registry.hasTypeHandler(obj.getClass())) {
                        value = obj;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
                               && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());

                        if (value != null) {
                            String key = propertyName.substring(prop.getName().length());
                            value = configuration.newMetaObject(value).getValue(key);
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }

                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        String message = String.format(
                                "TypeHandler is null 执行的 SQL 中找不到参数: %s ID: %s", propertyName, statement.getId());
                        throw new ExecutorException(message);
                    }

                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

    public static int getNumber(Object obj) {
        return Integer.parseInt(obj.toString());
    }

    public static <T> T getValueOrDefault(T obj, T def) {
        return obj == null ? def : obj;
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || "".equals(obj.toString());
    }

    public static boolean isEquals(Object obj1, Object obj2) {
        return !(isEmpty(obj1) || isEmpty(obj2)) && obj1.toString().equals(obj2.toString());
    }
}
