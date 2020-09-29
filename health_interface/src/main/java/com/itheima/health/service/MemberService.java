package com.itheima.health.service;

import com.itheima.health.pojo.Member;

/**
 * @Author: zengrui
 * @Date: 2020/9/29 11:34
 */
public interface MemberService {
    //通过手机号码查询会员信息
    Member findByTelephone(String telephone);

    //添加会员
    void add(Member member);
}
