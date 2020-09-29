package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengrui
 * @Date: 2020/9/23 16:29
 */
public interface OrderSettingService {
    //批量导入预约设置
    void add(List<OrderSetting> list) throws HealthException;

    //查询获取当前月份预约设置信息
    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    //基于日历的预约设置
    void editNumberByDate(OrderSetting orderSetting) throws HealthException;
}
