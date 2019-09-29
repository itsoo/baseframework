package com.github.webapp.module.demo;

import com.github.webapp.common.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * DemoServiceImpl
 *
 * @author zxy
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private Dao dao;

    private static final String NAMESPACE = "demoMapper.";

    @Override
    public int add(Map<String, Object> param) {
        return dao.add(NAMESPACE + "add", param);
    }

    @Override
    public int delete(Integer id) {
        return 0;
    }

    @Override
    public int update(Integer id) {
        return 0;
    }

    @Override
    public List<Map> queryList() {
        return dao.findForList(NAMESPACE + "queryList", null);
    }
}
