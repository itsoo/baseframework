package com.github.baseframework.pageplugin.toolkit;

import com.github.baseframework.pageplugin.PageInfo;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.PropertyException;
import java.util.Map;

/**
 * SqlGenerator
 *
 * @author zxy
 */
@Slf4j
public class SqlGenerator {

    /**
     * 组织查询总记录数 SQL
     *
     * @param sql String
     * @return count sql
     */
    public static String generateCountSql(String sql) {
        return String.format("SELECT count(*) FROM (%s) tmp_count", sql);
    }

    /**
     * 根据数据库方言组织分页 SQL
     *
     * @param dialect  String
     * @param sql      String
     * @param pageInfo Map
     * @return page sql
     */
    public static String generatePageSql(String dialect, String sql, Map<String, Object> pageInfo) {
        if (pageInfo != null) {
            int startRows = PageInfo.getStartRows(pageInfo);

            if (PageUtils.isEquals(PageInfo.Dialect.MYSQL, dialect)) {
                int pageSize = PageUtils.toNumber(pageInfo.get(PageInfo.PAGE_SIZE));

                return String.format("%s LIMIT %d, %d", sql, startRows, pageSize);
            } else if (PageUtils.isEquals(PageInfo.Dialect.ORACLE, dialect)) {
                String pageSql =
                        "SELECT * FROM (SELECT tmp_tb.*, rownum row_num FROM (%s) TMP_TB) WHERE row_num > %d AND row_num <= %d";

                return String.format(pageSql, sql, startRows, PageInfo.getEndRows(pageInfo));
            }

            log.error("SQL 分页异常", new PropertyException("未被分页组件支持的数据库: " + dialect));

            return sql;
        }

        return sql;
    }
}
