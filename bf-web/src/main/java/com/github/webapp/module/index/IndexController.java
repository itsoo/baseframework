package com.github.webapp.module.index;

import com.alibaba.fastjson.JSONObject;
import com.github.baseframework.core.pojo.User;
import com.github.baseframework.service.CasShiroService;
import com.github.webapp.common.config.ShiroConfig;
import com.github.webapp.module.common.BaseController;
import com.github.webapp.util.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * IndexController
 *
 * @author zxy
 */
@RestController
@RequestMapping("/")
public class IndexController extends BaseController {

    @Autowired
    ShiroConfig.ShiroProperties shiroProperties;

    @Autowired
    private CasShiroService casShiroService;

    @GetMapping
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        renderIndex(request, response);
    }

    @GetMapping("token")
    public void token(HttpServletRequest request, HttpServletResponse response) throws Exception {
        renderPage("token", request, response);
    }

    @GetMapping("query_user_permissions")
    public Map<String, Object> queryUserPermissions(HttpServletRequest request) {
        final Map<String, Object> result = new HashMap<>(4);

        User user = HttpRequestUtils.getCurrUser(request);
        if (user == null) {
            return Collections.emptyMap();
        }

        JSONObject json = casShiroService.queryPermissionTreeWithAll(
                user.getEmpNo(), shiroProperties.getProjectCode(), shiroProperties.getSecurityServer());
        result.put("permissions", json);
        result.put("user", user);

        return result;
    }
}
