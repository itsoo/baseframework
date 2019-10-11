package com.github.webapp.common.dao;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 默认公共持久层
 *
 * @author zxy
 */
@Repository("dao")
public class DefaultDaoImpl implements Dao {

    private final SqlSessionTemplate sqlSessionTemplate;

    public DefaultDaoImpl(@Qualifier("sqlSessionTemplate") SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public int add(String handlerId, Object param) {
        return sqlSessionTemplate.insert(handlerId, param);
    }

    @Override
    public <T> int batchAdd(String handlerId, List<T> params) {
        int result = 0;
        SqlSessionFactory factory = sqlSessionTemplate.getSqlSessionFactory();

        try (SqlSession sqlSession = factory.openSession(ExecutorType.BATCH, false)) {
            if (params != null) {
                for (T param : params) {
                    sqlSession.insert(handlerId, param);
                    result++;
                }

                sqlSession.flushStatements();
                sqlSession.commit();
                sqlSession.clearCache();
            }
        }

        return result;
    }

    @Override
    public int update(String handlerId, Object param) {
        return sqlSessionTemplate.update(handlerId, param);
    }

    @Override
    public <T> int batchUpdate(String handlerId, List<T> params) {
        int result = 0;
        SqlSessionFactory factory = sqlSessionTemplate.getSqlSessionFactory();

        try (SqlSession sqlSession = factory.openSession(ExecutorType.BATCH, false)) {
            if (params != null) {
                for (T param : params) {
                    sqlSession.update(handlerId, param);
                    result++;
                }

                sqlSession.flushStatements();
                sqlSession.commit();
                sqlSession.clearCache();
            }
        }

        return result;
    }

    @Override
    public int delete(String handlerId, Object param) {
        return sqlSessionTemplate.delete(handlerId, param);
    }

    @Override
    public <T> int batchDelete(String handlerId, List<T> params) {
        int result = 0;
        SqlSessionFactory factory = sqlSessionTemplate.getSqlSessionFactory();

        try (SqlSession sqlSession = factory.openSession(ExecutorType.BATCH, false)) {
            if (params != null) {
                for (T param : params) {
                    sqlSession.delete(handlerId, param);
                    result++;
                }

                sqlSession.flushStatements();
                sqlSession.commit();
                sqlSession.clearCache();
            }
        }

        return result;
    }

    @Override
    public <T> T findForObject(String handlerId, Object param) {
        return sqlSessionTemplate.selectOne(handlerId, param);
    }

    @Override
    public <T> List<T> findForList(String handlerId, Object param) {
        if (param instanceof Collection) {
            List<T> result = new LinkedList<>();

            for (Object obj : (Collection) param) {
                if (obj != null) {
                    result.add(sqlSessionTemplate.selectOne(handlerId, obj));
                }
            }

            return result;
        }

        return sqlSessionTemplate.selectList(handlerId, param);
    }
}
