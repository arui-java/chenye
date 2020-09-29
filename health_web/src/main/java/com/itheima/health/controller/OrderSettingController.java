package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengrui
 * @Date: 2020/9/23 14:37
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    private static final Logger log= LoggerFactory.getLogger(OrderSettingController.class);
    //批量导入预约设置
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            //读取内容
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //转成List<OrderSetting>
            List<OrderSetting> list=new ArrayList<OrderSetting>();
            OrderSetting os=null;
            //日期格式解析
            SimpleDateFormat sdf=new SimpleDateFormat(POIUtils.DATE_FORMAT);
            //strArr代表一行记录  0日期，1数量
            for (String[] strArr : strings) {
                Date orderDate = sdf.parse(strArr[0]);
                os=new OrderSetting(orderDate,Integer.valueOf(strArr[1]));
                list.add(os);
            }
            //调用服务
            orderSettingService.add(list);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("批量导入失败",e);
        }
        return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }

    //查询获取当前月份预约设置信息
    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        List<Map<String, Integer>> monthData=orderSettingService.getOrderSettingByMonth(month);
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,monthData);
    }

    //基于日历的预约设置
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        orderSettingService.editNumberByDate(orderSetting);
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
