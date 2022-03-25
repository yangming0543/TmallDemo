package com.xq.tmall.entity;

import lombok.Data;

/**
 * 管理员类
 */
@Data
public class Admin {
    private Integer admin_id;
    private String admin_name;
    private String admin_nickname;
    private String admin_password;
    private String admin_profile_picture_src;
}
