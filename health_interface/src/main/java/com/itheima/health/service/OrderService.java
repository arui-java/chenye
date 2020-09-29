package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Order;

import java.util.Map;

/**
 * @Author: zengrui
 * @Date: 2020/9/28 19:05
 */
public interface OrderService {
    //提交预约
    Order submit(Map<String, String> orderInfo)throws HealthException;

    //订单详情
    Map<String, String> findById(int id);
}
