package com.itheima.security;


import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

/**
 * @Author: zengrui
 * @Date: 2020/10/7 20:46
 */
public class UserService implements UserDetailsService {

    public User findByName(){
        System.out.println("findByName");
        return null;
    }
    public User findUser(){
        System.out.println("findUser");
        return null;
    }

    public User getUser(){
        System.out.println("getUser");
        return null;
    }

    //获取登录用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库查询
        User user=findByUsername(username);
        if (null != user){
            //用户存在
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            //用户所拥有的角色
            Set<Role> roles = user.getRoles();
            if (null!=roles&& roles.size()>0){
                // 有角色
                for (Role role : roles) {
                    // 授予角色 keyword关键字，必须以ROLE_打头
                    // ? 为什么要授予角色， 支持使用角色做权限控制
                    SimpleGrantedAuthority sga = new SimpleGrantedAuthority(role.getKeyword());
                    authorities.add(sga);
                    // 角色下的权限
                    Set<Permission> permissions = role.getPermissions();
                    if(null != permissions && permissions.size() >0){
                        // 授予权限  支持使用权限做权限控制
                        for (Permission permission : permissions) {
                            // 权限的关键字没有要求
                            sga = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sga);
                        }
                    }
                }
            }

            org.springframework.security.core.userdetails.User securityUser =
                    new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
            return securityUser;
        }
        return null;
    }

    //假设从数据库查
    private com.itheima.health.pojo.User findByUsername (String username){
        if("admin".equals(username)) {
            User user=new User();
            user.setUsername("admin");
            user.setPassword("$2a$10$aHWzucURMEO.OeHLzPaqxOXMJxyadfQExpVK6Keb8VhVvQUAYUaKa");

            // t_user > t_role > t_permission
            Role role = new Role();
            role.setName("管理员");
            role.setKeyword("ROLE_ADMIN");

            Permission permission = new Permission();
            permission.setName("新增检查项");
            permission.setKeyword("ADD_CHECKITEM");
            // 权限是属于某个角色下
            role.getPermissions().add(permission);

            Set<Role> roleList = new HashSet<Role>();
            roleList.add(role);

            // 多种校验规则
            role = new Role();
            role.setName("ABC");
            role.setKeyword("ABC");
            roleList.add(role);

            user.setRoles(roleList);
            // 把的用户对象，用户名，密码，用户下的角色，角色下还有权限
            return user;
        }
        return null;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        //加密
       // System.out.println(encoder.encode("1234"));
       // System.out.println(encoder.encode("1234"));
        System.out.println(encoder.encode("admin"));

        //验证密码
        System.out.println(encoder.matches("1234", "$2a$10$qFWlbFo7f.uZ50N.iIYH2OzXn.ocppku3GFOz/28ALzFlHpBqBNju"));
        System.out.println(encoder.matches("1234", "$2a$10$cbxYkxUpVL769fi/K3xv4uh4dkXoHdT1DEQaSwQQqFsG6G594BBky"));
        System.out.println(encoder.matches("1234", "$2a$10$D.pxlAaYI1JkHdw77AbUlOq.Nc2/4VVGlQcweY356XBPwSw3aPEim"));
        System.out.println(encoder.matches("1234","$2a$10$u/BcsUUqZNWUxdmDhbnoeeobJy6IBsL1Gn/S0dMxI2RbSgnMKJ.4a"));
    }


}
