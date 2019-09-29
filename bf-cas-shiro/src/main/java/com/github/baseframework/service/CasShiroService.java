package com.github.baseframework.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CasShiroService {

    /**
     * 调用接口查询用户、权限
     *
     * @param account     String
     * @param projectCode String
     * @param url         String
     * @return Map
     */
    Map queryUserAndRolesAndPermissions(String account, String projectCode, String url);

    /**
     * 查询权用户限树
     *
     * @param account     String
     * @param projectCode String
     * @param url         String
     * @return PermissionNote
     */
    JSONObject queryPermissionTreeWithAll(String account, String projectCode, String url);

    /**
     * 查询项目列表
     *
     * @param url String
     * @return List
     */
    List getProjectList(String url);

    /**
     * 调用 URL 并返回 JSON 字符串
     *
     * @param url String
     * @return String
     * @throws IOException IOException
     */
    String responseUrl(String url) throws IOException;

    /**
     * 获取组织机构下所有用户
     *
     * @param securityServer String
     * @return List
     */
    List getUserGroup(String securityServer);
}
