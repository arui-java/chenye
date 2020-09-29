package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengrui
 * @Date: 2020/9/23 16:31
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> list) throws HealthException {
        if (null!=list && list.size()>0){
            //遍历
            for (OrderSetting orderSetting:list){
                //通过日期查询预约设置信息
                OrderSetting osInDB=  orderSettingDao.findByOrderData(orderSetting.getOrderDate());
                //存在
                if (null!=osInDB){
                    //判断要设置的最大预约数量<已预约数量
                    if (orderSetting.getNumber()<osInDB.getReservations()){
                        //小于就报错
                        throw new HealthException("最大预约的数量不能小于已预约数量");
                    }
                    //否，通过日期更新最大预约数
                    orderSettingDao.updateNumber(orderSetting);
                }else {
                    //不存在，插入预约设置
                    orderSettingDao.add(orderSetting);
                }
            }

        }
    }

    //查询获取当前月份预约设置信息
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        //拼接开始日期和结束日期
        String startDate = month + "-01";
        String endDate = month + "-31";
        List<Map<String, Integer>> monthData = orderSettingDao.getOrderSettingBetween(startDate,endDate);
      /*  Map map = new HashMap();
        map.put("startDate",startDate);
        map.put("endDate",endDate);*/
        // 2.查询当前月份的预约设置
       // List<Map<String, Integer>> monthData = orderSettingDao.getOrderSettingBetween(map);
        return monthData;
    }

    //基于日历的预约设置
    @Override
    public void editNumberByDate(OrderSetting orderSetting) throws HealthException{
        //通过日期判断预约设置信息是否存在
        OrderSetting osInDB=  orderSettingDao.findByOrderData(orderSetting.getOrderDate());
        //存在
        if (null!=osInDB){
            //判断已经预约的人数是否大于要更新的最大可预约人数
            if (orderSetting.getNumber()<osInDB.getReservations()){
                //小于就报错
                throw new HealthException("最大预约的人数不能小于已预约人数");
            }
            //否，通过日期更新最大预约数，
            // reverations <= 传进来的number数量，则要更新最大可预约数量
            orderSettingDao.updateNumber(orderSetting);
        }else {
            //不存在，插入预约设置
            orderSettingDao.add(orderSetting);
        }
    }

}
