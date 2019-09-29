package com.github.baseframework.core.realm;

import com.github.baseframework.core.pojo.User;
import com.github.baseframework.service.CasShiroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户授权信息域
 *
 * @author zxy
 */
@Slf4j
public class UserRealm extends CasRealm {

    @Resource
    protected CasShiroService casShiroService;

    /**
     * 角色权限接口地址
     */
    protected String url;

    /**
     * 项目编号
     */
    protected String projectCode;

    public void setUrl(String url) { this.url = url; }

    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }

    @Override
    protected TicketValidator createTicketValidator() {
        Cas30ServiceTicketValidator cas30ServiceTicketValidator =
                new Cas30ServiceTicketValidator(getCasServerUrlPrefix());
        cas30ServiceTicketValidator.setEncoding("UTF-8");

        return cas30ServiceTicketValidator;
    }

    /**
     * 设置角色和权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Map map = (Map) principals.asList().get(1);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        int times = 3;

        while (times > 0) {
            try {
                String username = map.get("username").toString();
                Map user = casShiroService.queryUserAndRolesAndPermissions(username, projectCode, url);
                authorizationInfo.addRoles((List) user.get("roles"));
                authorizationInfo.addStringPermissions((List) user.get("permissions"));

                break;
            } catch (Exception e) {
                times--;
                log.error(e.getMessage(), e);
            }
        }

        return authorizationInfo;
    }

    /**
     * 1、CAS认证 ,验证用户身份
     * 2、将用户基本信息设置到会话中
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        AuthenticationInfo authc = superDoGetAuthenticationInfo(token);
        if (authc == null) {
            return null;
        }

        Map map = (Map) authc.getPrincipals().asList().get(1);

        User user = new User();
        user.setEmpNo(map.get("username").toString());
        user.setName(map.get("name").toString());
        user.setStatus(map.get("status").toString());
        user.setId(map.get("id") != null ? Integer.parseInt(map.get("id").toString()) : null);

        SecurityUtils.getSubject().getSession().setAttribute("user", user);

        return authc;
    }

    protected AuthenticationInfo superDoGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        CasToken casToken = (CasToken) token;
        if (token == null) {
            return null;
        }

        String ticket = (String) casToken.getCredentials();
        if (!StringUtils.hasText(ticket)) {
            return null;
        }

        try {
            AttributePrincipal casPrincipal =
                    ensureTicketValidator().validate(ticket, getCasService()).getPrincipal();

            if (log.isDebugEnabled()) {
                log.debug("Validate ticket : {} in CAS server : {} to retrieve user : {}",
                          ticket, getCasServerUrlPrefix(), casPrincipal.getName());
            }

            Map<String, Object> attributes = casPrincipal.getAttributes();
            casToken.setUserId(casPrincipal.getName());

            if (Boolean.parseBoolean((String) attributes.get(getRememberMeAttributeName()))) {
                casToken.setRememberMe(true);
            }

            String name = String.format("%s(%s)", attributes.get("name"), casPrincipal.getName());

            return new SimpleAuthenticationInfo(
                    new SimplePrincipalCollection(CollectionUtils.asList(name, attributes), getName()), ticket);
        } catch (TicketValidationException e) {
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        }
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}
