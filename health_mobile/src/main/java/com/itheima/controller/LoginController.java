package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Author: zengrui
 * @Date: 2020/9/29 11:10
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    //手机快速登录
    @PostMapping("/check")
    public Result checkMember(@RequestBody Map<String,String> loginInfo, HttpServletResponse res){
        //验证码校验
        String telephone = loginInfo.get("telephone");
        //前端提交的验证码与redis的验证码是否一致
        String key= RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone;
        //从redis中取出验证码codeInRedis key=手机号码
        Jedis jedis = jedisPool.getResource();
        String codeInRedis = jedis.get(key);
        //codeInRedis没有值，提示用户重新获取验证码
        if (StringUtils.isEmpty(codeInRedis)){
            return new Result(false,"请重新获取验证码");
        }
        //有值，则比较验证码与前端的是否一致
        if (!codeInRedis.equals(loginInfo.get("validateCode"))){
            //不一样，验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        //防止重复提交，移除验证码
        jedis.del(key);

        //通过手机号判断是否为会员
        Member member= memberService.findByTelephone(telephone);
        //不是会员，就注册
        if (null==member){
            member=new Member();
            member.setRegTime(new Date());
            member.setRemark("快速登录");
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }
        //添加cookie跟踪，手机号是用户标识
        Cookie cookie=new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(30*24*60*60);
        cookie.setPath("/");// 当用户访问哪些路径时就会带上这个cookie ，所有
        res.addCookie(cookie);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
