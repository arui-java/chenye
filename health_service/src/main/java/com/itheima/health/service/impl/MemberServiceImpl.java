package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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

    //统计过去一年的会员总数
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        List<Integer> memberCount=new ArrayList<Integer>(months.size());
        if (months!=null){
            //循环12个月，每个月查询一下
            for (String month : months) {
               Integer conunt = memberDao.findMemberCountBeforeDate(month+"-31");
                memberCount.add(conunt);
            }
        }
        return memberCount;
    }
}
