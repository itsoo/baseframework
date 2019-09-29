package com.github.baseframework.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.baseframework.core.pojo.ResultData;
import com.github.baseframework.service.CasShiroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CasShiroServiceImpl implements CasShiroService {

    @Override
    public Map queryUserAndRolesAndPermissions(String account, String projectCode, String url) {
        String curl = String.format("%s/api/getRoleAndPermission/%s/%s", url, account, projectCode);

        try {
            return (Map) JSONObject.parseObject(responseUrl(curl), HashMap.class).get("result");
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return null;
        }
    }

    @Override
    public JSONObject queryPermissionTreeWithAll(String account, String projectCode, String url) {
        String curl = String.format(
                "%s/api/queryPermissionTree?empNo=%s&projectCode=%s&withAll=%s", url, account, projectCode, true);

        try {
            return (JSONObject) JSONObject.parseObject(responseUrl(curl), ResultData.class).getResult().get("list");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List getProjectList(String url) {
        String curl = String.format("%s/api/getProjectList", url);

        try {
            return JSONObject.parseObject(responseUrl(curl), List.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public String responseUrl(String url) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
                    return response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : null;
                }

                throw new ClientProtocolException("Unexpected response status: " + status);
            };

            return httpclient.execute(new HttpGet(url), responseHandler);
        }
    }

    @Override
    public List getUserGroup(String securityServer) {
        String curl = String.format("%s/api/getUserGroup", securityServer);

        try {
            return (List) JSONObject.parseObject(responseUrl(curl), ResultData.class).getResult().get("list");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
