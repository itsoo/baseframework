package com.github.baseframework.pageplugin.pointcut;

import com.github.baseframework.pageplugin.PageInfo;
import com.github.baseframework.pageplugin.ReflectHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页切面
 *
 * @author zxy
 */
@Slf4j
@Aspect
@Component
public class PageAspect {

    /**
     * 切点环绕通知
     *
     * @param point ProceedingJoinPoint
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("@annotation(com.github.baseframework.pageplugin.annotation.Page)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] objects = point.getArgs();

        // 记录包含分页参数下标
        int index = getTargetIndex(point.getArgs());

        // 未得到分页参数的入参
        if (index == -1) {
            Map<String, Object> pageInfo;

            for (Object object : objects) {
                pageInfo = setPageInfo(point.proceed(), object);
                if (pageInfo != null) {
                    return pageInfo;
                }
            }

            return null;
        }

        return setPageInfo(point.proceed(), objects[index]);
    }

    /**
     * 获取入参包含 pageNum 或 pageSize 属性容器
     *
     * @param objects Object[]
     * @return int
     * @throws NoSuchFieldException   NoSuchFieldException
     * @throws IllegalAccessException IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    private int getTargetIndex(Object[] objects) throws NoSuchFieldException, IllegalAccessException {
        Object obj;
        Map<String, Object> pageInfo;

        for (int i = 0, len = objects.length; i < len; i++) {
            obj = objects[i];

            // 参数为 Map 类型
            if (obj instanceof Map) {
                pageInfo = new HashMap((Map) obj);
                if (hasKey4PageInfo(pageInfo)) {
                    return i;
                }

                continue;
            }

            // 参数为其它 Bean
            pageInfo = (Map) ReflectHelper.getValueByFieldName(obj, PageInfo.PAGE);
            if (hasKey4PageInfo(pageInfo)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 移除分页相关信息
     *
     * @param pageInfo Map
     */
    private void removePageInfos(Map<String, Object> pageInfo) {
        pageInfo.remove(PageInfo.PAGE_NUM);
        pageInfo.remove(PageInfo.PAGE_SIZE);
        pageInfo.remove(PageInfo.TOTAL_PAGE);
        pageInfo.remove(PageInfo.TOTAL_COUNT);
        pageInfo.remove(PageInfo.HAS_PRE_PAGE);
        pageInfo.remove(PageInfo.HAS_NEXT_PAGE);
        pageInfo.remove(PageInfo.PAGE_LIST);
    }

    /**
     * 入参包含分页参数
     *
     * @param pageInfo Map
     * @return boolean
     */
    private boolean hasKey4PageInfo(Map<String, Object> pageInfo) {
        return pageInfo != null && pageInfo.get(PageInfo.PAGE_NUM) != null;
    }

    /**
     * 设置分页相关信息
     *
     * @param arc Object
     * @param obj Object
     * @return Map
     * @throws Throwable Throwable
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> setPageInfo(Object arc, Object obj) throws Throwable {
        // String 及包装类型返回 null
        if (obj instanceof String || obj instanceof Double || obj instanceof Long
            || obj instanceof Integer || obj instanceof Character || obj instanceof Boolean) {
            return null;
        }

        // 参数为 Map 类型
        Map<String, Object> pageInfo;

        if (obj instanceof Map) {
            pageInfo = new HashMap((Map) obj);
            PageInfo.getInstance(pageInfo, arc);
            Map<String, Object> page = (Map) pageInfo.remove(PageInfo.PAGE);

            if (page != null) {
                pageInfo.putAll(page);
            }

            if (PageInfo.isFalsePage(obj)) {
                removePageInfos(pageInfo);
            }

            pageInfo.put(PageInfo.DATA_LIST, arc);
        } else { // 参数为其它 Bean
            try {
                pageInfo = (Map) ReflectHelper.getValueByFieldName(obj, PageInfo.PAGE);
            } catch (Exception e) {
                throw new IllegalArgumentException("POJO 缺少必要的 Map page 属性");
            }

            if (null == pageInfo) {
                pageInfo = new HashMap(16);
            }

            PageInfo.getInstance(pageInfo, arc);
            ReflectHelper.setValueByFieldName(obj, PageInfo.PAGE, pageInfo);
        }

        return pageInfo;
    }
}
