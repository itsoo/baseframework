package com.github.baseframework.pageplugin;

import com.github.baseframework.pageplugin.util.PageUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 结果页
 *
 * @author zxy
 */
public final class PageInfo {

    public static final String PAGE_LIST = "pageList";        // 控制分页属性 ("false"不分页)
    public static final String PAGE = "page";                 // 封装 page 属性
    public static final String PAGE_NUM = "pageNum";          // 当前页数
    public static final String PAGE_SIZE = "pageSize";        // 页容量
    public static final String TOTAL_PAGE = "totalPage";      // 总页数
    public static final String TOTAL_COUNT = "totalCount";    // 总记录数
    public static final String DATA_LIST = "dataList";        // 查询结果集
    public static final String HAS_PRE_PAGE = "hasPrePage";   // 有上一页
    public static final String HAS_NEXT_PAGE = "hasNextPage"; // 有下一页

    static final String DIALECT = "dialect";                  // 数据库方言
    static final String REGEXP = "regexp";                    // 拦截的 SQL ID
    static Integer defaultPageSize = 10;                      // 默认页容量

    /**
     * 私有的构造
     */
    private PageInfo() {}

    /**
     * 实例化方法
     *
     * @return Map
     */
    static Map<String, Object> getInstance() {
        Map<String, Object> map = new HashMap<>(16);
        map.put(PAGE_NUM, 1);
        map.put(PAGE_SIZE, defaultPageSize);
        map.put(TOTAL_PAGE, 0);
        map.put(TOTAL_COUNT, 0);
        map.put(DATA_LIST, null);
        map.put(HAS_PRE_PAGE, false);
        map.put(HAS_NEXT_PAGE, false);

        return map;
    }

    /**
     * 实例化方法
     *
     * @param map Map
     * @return Map
     */
    static Map<String, Object> getInstance(Map<String, Object> map) {
        setPageInfo(map);
        setPageNum(map);
        setPageSize(map);
        map.put(DATA_LIST, null);
        setHasPrePage(map);
        setHasNextPage(map);

        return map;
    }

    /**
     * 实例化方法
     *
     * @param map      Map
     * @param dataList Object
     */
    public static void getInstance(Map<String, Object> map, Object dataList) {
        getInstance(map);
        map.put(DATA_LIST, dataList);
    }

    /**
     * 得到分页起始行号
     *
     * @return int
     */
    public static int getStartRows(Map<String, Object> map) {
        int pageNum = PageUtil.getNumber(map.get(PAGE_NUM));
        int pageSize = PageUtil.getNumber(map.get(PAGE_SIZE));

        return (pageNum - 1) * pageSize;
    }

    /**
     * 得到分页结束行号
     *
     * @return int
     */
    public static int getEndRows(Map<String, Object> map) {
        int pageNum = PageUtil.getNumber(map.get(PAGE_NUM));
        int pageSize = PageUtil.getNumber(map.get(PAGE_SIZE));

        return pageNum * pageSize;
    }

    /**
     * 钩子方法判断是否需要分页
     *
     * @param obj Object
     * @return boolean
     */
    public static boolean isFalsePage(Object obj) {
        if (obj instanceof Map) {
            return PageUtil.isEquals(((Map) obj).get(PAGE_LIST), "false");
        }

        Object pageList = null;

        try {
            pageList = ReflectHelper.getValueByFieldName(obj, PAGE_LIST);
        } catch (Exception e) {
            try {
                pageList = ((Map) ReflectHelper.getValueByFieldName(obj, PAGE)).get(PAGE_LIST);
            } catch (Exception ignore) {}
        }

        return PageUtil.isEquals(pageList, "false");
    }

    /**
     * SETTER METHOD
     */
    private static void setPageInfo(Map<String, Object> map) {
        String[] keys = {PAGE_NUM, PAGE_SIZE, TOTAL_PAGE, TOTAL_COUNT, DATA_LIST, HAS_PRE_PAGE, HAS_NEXT_PAGE};
        Object[] objs = new Object[keys.length];

        for (int i = 0; i < keys.length; i++) {
            objs[i] = map.get(keys[i]);
        }

        map.putAll(getInstance());

        // 原值转换
        for (int i = 0; i < objs.length; i++) {
            setMapKeyValue(map, keys[i], objs[i]);
        }
    }

    private static void setMapKeyValue(Map<String, Object> map, String key, Object value) {
        if (!PageUtil.isEmpty(value)) {
            map.put(key, value);
        }
    }

    private static void setPageNum(Map<String, Object> map) {
        // 总页数
        setTotalPage(map);

        Object pageNum = map.get(PAGE_NUM);
        Object totalPage = map.get(TOTAL_PAGE);

        if (PageUtil.isEmpty(pageNum)) {
            pageNum = 1;
        } else {
            int pn = PageUtil.getNumber(pageNum);
            int tp = PageUtil.getNumber(totalPage);

            pageNum = Math.min(Math.max(1, pn), tp);
        }

        map.put(PAGE_NUM, pageNum);
    }

    private static void setPageSize(Map<String, Object> map) {
        Object pageSize = map.get(PAGE_SIZE);

        if (PageUtil.isEmpty(pageSize)) {
            map.put(PAGE_SIZE, defaultPageSize);
        } else {
            map.put(PAGE_SIZE, PageUtil.getNumber(pageSize));
        }
    }

    private static void setTotalPage(Map<String, Object> map) {
        Object tcObj = map.get(TOTAL_COUNT);
        Object psObj = map.get(PAGE_SIZE);

        int totalPage = 1;

        if (!PageUtil.isEmpty(tcObj) && !PageUtil.isEmpty(psObj)) {
            int totalCount = PageUtil.getNumber(tcObj);
            int pageSize = PageUtil.getNumber(psObj);

            totalPage = (int) Math.ceil(((double) totalCount) / pageSize);
        }

        map.put(TOTAL_PAGE, totalPage);
    }

    private static void setHasPrePage(Map<String, Object> map) {
        map.put(HAS_PRE_PAGE, PageUtil.getNumber(map.get(PAGE_NUM)) > 1);
    }

    private static void setHasNextPage(Map<String, Object> map) {
        int pageNum = PageUtil.getNumber(map.get(PAGE_NUM));
        int totalPage = PageUtil.getNumber(map.get(TOTAL_PAGE));

        map.put(HAS_NEXT_PAGE, pageNum < totalPage);
    }

    /**
     * 数据库方言
     */
    public interface Dialect {

        String MYSQL = "mysql";
        String ORACLE = "oracle";
    }
}
