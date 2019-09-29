package com.github.baseframework.ext.tags;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.tags.PermissionTag;

public class HasAnyPermissionTag extends PermissionTag {

    @Override
    protected boolean showTagBody(String permissions) {
        Subject subject = getSubject();
        if (subject != null) {
            for (String role : permissions.split(ShiroTags.SPLIT_STR)) {
                if (subject.isPermitted(role.trim())) {
                    return true;
                }
            }
        }

        return false;
    }
}
