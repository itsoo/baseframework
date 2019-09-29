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
    public int batchAdd(String handlerId, List params) {
        int result = 0;
        SqlSessionFactory factory = sqlSessionTemplate.getSqlSessionFactory();

        try (SqlSession sqlSession = factory.openSession(ExecutorType.BATCH, false)) {
            for (int i = 0, size = params == null ? 0 : params.size(); i < size; i++) {
                sqlSession.insert(handlerId, params.get(i));
                result++;
            }

            sqlSession.flushStatements();
            sqlSession.commit();
            sqlSession.clearCache();
        }

        return result;
    }

    @Override
    public int update(String handlerId, Object param) {
        return sqlSessionTemplate.update(handlerId, param);
    }

    @Override
    public int batchUpdate(String handlerId, List params) {
        int result = 0;
        SqlSessionFactory factory = sqlSessionTemplate.getSqlSessionFactory();

        try (SqlSession sqlSession = factory.openSession(ExecutorType.BATCH, false)) {
            for (int i = 0, size = params == null ? 0 : params.size(); i < size; i++) {
                sqlSession.update(handlerId, params.get(i));
                result++;
            }

            sqlSession.flushStatements();
            sqlSession.commit();
            sqlSession.clearCache();
        }

        return result;
    }

    @Override
    public int delete(String handlerId, Object param) {
        return sqlSessionTemplate.delete(handlerId, param);
    }

    @Override
    public int batchDelete(String handlerId, List params) {
        int result = 0;
        SqlSessionFactory factory = sqlSessionTemplate.getSqlSessionFactory();

        try (SqlSession sqlSession = factory.openSession(ExecutorType.BATCH, false)) {
            for (int i = 0, size = params == null ? 0 : params.size(); i < size; i++) {
                sqlSession.delete(handlerId, params.get(i));
                result++;
            }

            sqlSession.flushStatements();
            sqlSession.commit();
            sqlSession.clearCache();
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

            for (Object o : (Collection) param) {
                T t = sqlSessionTemplate.selectOne(handlerId, o);

                if (null != t) {
                    result.add(t);
                }
            }

            return result;
        }

        return sqlSessionTemplate.selectList(handlerId, param);
    }
}
