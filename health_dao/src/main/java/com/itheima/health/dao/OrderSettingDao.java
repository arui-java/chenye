package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengrui
 * @Date: 2020/9/23 16:49
 */
public interface OrderSettingDao {
    //通过日期查询预约设置信息
    OrderSetting findByOrderData(Date orderDate);

    //通过日期更新最大预约数
    void updateNumber(OrderSetting orderSetting);

    //插入预约设置
    void add(OrderSetting orderSetting);

    //查询获取当前月份预约设置信息
    //List<Map<String, Integer>> getOrderSettingBetween(Map map);

    List<Map<String, Integer>> getOrderSettingBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    //更新已预约人数
    int editReservationsByOrderDate(OrderSetting orderSetting);
}
