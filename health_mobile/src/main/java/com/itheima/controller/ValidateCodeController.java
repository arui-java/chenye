package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author: zengrui
 * @Date: 2020/9/26 20:29
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    private static  final Logger log= LoggerFactory.getLogger(ValidateCodeUtils.class);

    @Autowired
    private JedisPool jedisPool;

    //发送手机验证码
    @PostMapping("/send4Order")
    public Result send4Order(String telephone){
        //判断是否发送过，通过key获取redis中的验证码
        Jedis jedis = jedisPool.getResource();
        String key= RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone;
        String codeInRedis = jedis.get(key);
        //存在，发过了
        //- 存在 发送过了
        if(!StringUtils.isEmpty(codeInRedis)){
            return new Result(false, "验证码已经发送过了，请注意查询");
        }
        //不存在
        //生成验证码
        String validateCode = ValidateCodeUtils.generateValidateCode(6) + "";
        try {
            //调用smsutils发送
            log.debug("给手机号码:{} 发送验证码:{}", telephone, validateCode);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
            //存入redis,设置有效期为10分钟
            jedis.setex(key,60*10,validateCode);
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            log.error(String.format("给手机号码:%s 发送验证码:%s 发送失败",telephone,validateCode),e);
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        } finally {
            jedis.close();
        }
    }

    //发送登录手机验证码
    @PostMapping("/send4Login")
    public Result send4Login(String telephone){
        //判断是否发送过，通过key获取redis中的验证码
        Jedis jedis = jedisPool.getResource();
        String key= RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone;
        String codeInRedis = jedis.get(key);
        //存在，发过了
        //- 存在 发送过了
        if(!StringUtils.isEmpty(codeInRedis)){
            return new Result(false, "验证码已经发送过了，请注意查询");
        }
        //不存在
        //生成验证码
        String validateCode = ValidateCodeUtils.generateValidateCode(6) + "";
        try {
            //调用smsutils发送
            log.debug("给手机号码:{} 发送验证码:{}", telephone, validateCode);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
            //存入redis,设置有效期为10分钟
            jedis.setex(key,60*10,validateCode);
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            log.error(String.format("给手机号码:%s 发送验证码:%s 发送失败",telephone,validateCode),e);
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        } finally {
            jedis.close();
        }
    }
}
