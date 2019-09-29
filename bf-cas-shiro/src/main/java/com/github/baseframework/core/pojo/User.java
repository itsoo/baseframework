package com.github.baseframework.core.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class User implements Serializable {

    private Integer id;
    private String empNo;
    private String password;
    private String name;
    private String status;
    private Date createTime;
    private String phoneNo;
    private String email;
    private Integer groupId;
    private String suNingNo;
    private List<String> roles;
    private List<String> permissions;
}
