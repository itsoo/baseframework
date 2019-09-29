package com.github.baseframework.ext.tags;

import com.github.baseframework.ext.freemarker.*;
import freemarker.template.SimpleHash;

/**
 * Created by zxy on 2017/11/15.
 */
public class ShiroTags extends SimpleHash {

    static final String SPLIT_STR = ",";

    public ShiroTags() {
        this.put("authenticated", new AuthenticatedTag());
        this.put("guest", new GuestTag());
        this.put("hasAnyRoles", new HasAnyRolesTag());
        this.put("hasPermission", new HasPermissionTag());
        this.put("hasAnyPermission", new FreeMarkerHasAnyPermissionTag());
        this.put("hasRole", new HasRoleTag());
        this.put("lacksPermission", new LacksPermissionTag());
        this.put("lacksRole", new LacksRoleTag());
        this.put("notAuthenticated", new NotAuthenticatedTag());
        this.put("principal", new PrincipalTag());
        this.put("user", new UserTag());
    }
}
