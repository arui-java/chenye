package com.itheima.health.secutity;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: zengrui
 * @Date: 2020/10/8 18:01
 */
@Component
public class SpringSecurityUserService  implements UserDetailsService {

    @Reference
    private UserService userService;

    //实现用户信息查询与授权
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库中获取信息
        User user=userService.findByUsername(username);
        if (null!=user){
            //用户存在
            //用户的权限集合
            List<GrantedAuthority> authorityList=new ArrayList<GrantedAuthority>();
            GrantedAuthority authority=null;
            //用户拥有的角色
            Set<Role> roles = user.getRoles();
            if (null!=roles){
                for (Role role : roles) {
                    //授予角色
                    authority=new SimpleGrantedAuthority(role.getKeyword());
                    authorityList.add(authority);
                    //角色下的权限集合
                    Set<Permission> permissions=role.getPermissions();
                    if (null!=permissions){
                        for (Permission permission : permissions) {
                            //授予权限
                            authority=new SimpleGrantedAuthority(permission.getKeyword());
                            authorityList.add(authority);
                        }
                    }
                }
            }
            //认证用户信息
            return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorityList);
        }
        //报错
        return null;
    }
}
