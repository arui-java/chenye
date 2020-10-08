package com.itheima.health.service;

import com.itheima.health.pojo.User;

/**
 * @Author: zengrui
 * @Date: 2020/10/8 18:13
 */
public interface UserService {
    //获取用户的角色权限信息
    User findByUsername(String username);
}
