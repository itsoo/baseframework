package com.github.webapp.common.dao;

import java.util.List;

/**
 * 公共持久层
 *
 * @author zxy
 */
public interface Dao {

    /**
     * 新增
     *
     * @param handlerId String
     * @param param     Object
     * @return int
     */
    int add(String handlerId, Object param);

    /**
     * 批量新增
     *
     * @param handlerId String
     * @param params    List
     * @return int
     */
    <T> int batchAdd(String handlerId, List<T> params);

    /**
     * 修改
     *
     * @param handlerId String
     * @param param     Object
     * @return int
     */
    int update(String handlerId, Object param);

    /**
     * 批量修改
     *
     * @param handlerId String
     * @param params    List
     * @return int
     */
    <T> int batchUpdate(String handlerId, List<T> params);

    /**
     * 删除
     *
     * @param handlerId String
     * @param param     Object
     * @return int
     */
    int delete(String handlerId, Object param);

    /**
     * 批量删除
     *
     * @param handlerId String
     * @param params    List
     * @return int
     */
    <T> int batchDelete(String handlerId, List<T> params);

    /**
     * 单条查询
     *
     * @param handlerId String
     * @param param     Object
     * @return <T>
     */
    <T> T findForObject(String handlerId, Object param);

    /**
     * 列表查询
     *
     * @param handlerId String
     * @param param     Object
     * @return List<T>
     */
    <T> List<T> findForList(String handlerId, Object param);
}
