package com.github.webapp.module.demo;

import java.util.List;
import java.util.Map;

/**
 * DemoService
 *
 * @author zxy
 */
public interface DemoService {

    /**
     * add
     *
     * @param param Map
     * @return int
     */
    int add(Map<String, Object> param);

    /**
     * delete
     *
     * @param id Integer
     * @return int
     */
    int delete(Integer id);

    /**
     * update
     *
     * @param id Integer
     * @return int
     */
    int update(Integer id);

    /**
     * queryList
     *
     * @return List<Map>
     */
    List<Map> queryList();
}
