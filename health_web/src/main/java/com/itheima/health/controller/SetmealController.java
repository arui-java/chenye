package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: zengrui
 * @Date: 2020/9/21 20:51
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    private static final Logger log=LoggerFactory.getLogger(SetmealController.class);

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;
    //套餐上传图片
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //获得原文件名
        String originalFilename = imgFile.getOriginalFilename();
        //截取原文件名，来获取拼接后缀名
        String extension= originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID生成唯一文件名+后缀名
        String uniqueName= UUID.randomUUID()+extension;

        try {
            //调用七牛云上传文件
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),uniqueName);
            //响应结果给页面
            //封装结果到map
            Map<String,String> map=new HashMap<String, String>();
            //map中有2个key
            //domain
            map.put("domain",QiNiuUtils.DOMAIN);
            //imgName
            map.put("imgName",uniqueName);
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            // e.printStackTrace();
            log.error("上传图片失败",e);
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    //添加套餐
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        // 调用服务添加套餐
        Integer setmealId = setmealService.add(setmeal, checkgroupIds);
        // 添加redis生成静态页面的任务
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key, setmealId+"|1|" + System.currentTimeMillis());
        jedis.close();
        //响应结果
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //分页查询
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> pageResult= setmealService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    //通过id查套餐
    @GetMapping("/findById")
    public Result findById(int id){
       Setmeal setmeal = setmealService.findById(id);

       //显示图片，图片路径封装map,
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("setmeal",setmeal); //forData
        map.put("domain",QiNiuUtils.DOMAIN); //domain
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,map);
    }
    //通过id查询选中的检查组ids
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        List<Integer> checkgroupIds= setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,checkgroupIds);
    }
    //修改更新套餐
    @PostMapping("/update")
    public Result update (@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        setmealService.update(setmeal,checkgroupIds);

        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key, setmeal.getId()+"|1|" + System.currentTimeMillis());
        jedis.close();

        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    //删除套餐
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        setmealService.deleteById(id);

        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key, id+"|0|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true,"删除套餐成功!");
    }

}
