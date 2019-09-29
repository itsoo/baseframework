package com.github.baseframework.util;

import com.github.baseframework.core.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Shiro 工具类
 *
 * @author zxy
 */
public class ShiroUtils {

    public static Session getSession() {
        return getSubject().getSession();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static User getUserEntity() {
        return (User) getSession().getAttribute("user");
    }

    public static String getEmpNo() {
        return getUserEntity() == null ? null : getUserEntity().getEmpNo();
    }

    public static Integer getUserId() {
        return getUserEntity() == null ? null : getUserEntity().getId();
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return getSubject().getPrincipal() != null;
    }

    public static void logout() {
        getSubject().logout();
    }

    public static String md5(String word) {
        return new Md5Hash(word).toString();
    }
}
