package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zengrui
 * @Date: 2020/9/29 11:35
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    //通过手机号码查询会员信息
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //添加会员
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }
}
