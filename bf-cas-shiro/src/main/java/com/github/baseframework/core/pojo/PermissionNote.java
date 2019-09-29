package com.github.baseframework.core.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 权限树
 *
 * @author zhouhy
 */
@Data
@ToString
public class PermissionNote {

    private String id;
    private String name;
    private String permissions;
    private String url;
    private String icon;
    private String orderNum;
    private String parentId;
    private String type;
    private List<PermissionNote> childNote;
}
