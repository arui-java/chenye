package com.itheima.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: zengrui
 * @Date: 2020/9/20 18:22
 */

/*
 * Description: 全局异常处理
 * Advice: 通知
 * User: Eric
 * ExceptionHandler 获取的异常 异常的范围从小到大，类似于try catch中的catch
 * 与前端约定好的，返回的都是json数据
 */
@RestControllerAdvice
public class HealExceptionAdvice {

    private static  final Logger log= LoggerFactory.getLogger(HealExceptionAdvice.class);

    //自定义抛出的异常处理
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException he){
        //我们自己抛出的异常，把异常信息包装下返回即可
        return new Result(false,he.getMessage());
    }

    //所有未知的异常处理
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error("发生异常",e);
        return new Result(false,"发生未知错误，操作失败，请联系管理员");
    }
}
