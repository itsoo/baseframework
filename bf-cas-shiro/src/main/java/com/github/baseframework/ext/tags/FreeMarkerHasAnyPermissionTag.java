package com.github.baseframework.ext.tags;

import com.github.baseframework.ext.freemarker.PermissionTag;

/**
 * 扩展标签，具有列出权限中的任意一个
 */
public class FreeMarkerHasAnyPermissionTag extends PermissionTag {

    @Override
    protected boolean showTagBody(String permissions) {
        for (String role : permissions.split(ShiroTags.SPLIT_STR)) {
            if (isPermitted(role.trim())) {
                return true;
            }
        }

        return false;
    }
}
