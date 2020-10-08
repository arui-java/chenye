package com.itheima.health.dao;

import com.itheima.health.pojo.User;

/**
 * @Author: zengrui
 * @Date: 2020/10/8 18:17
 */
public interface UserDao {
    //获取用户的角色权限信息
    User findByUsername(String username);
}
