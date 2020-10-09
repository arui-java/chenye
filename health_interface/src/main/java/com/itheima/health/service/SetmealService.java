package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengrui
 * @Date: 2020/9/21 21:32
 */
public interface SetmealService {
    //添加套餐
    Integer add(Setmeal setmeal, Integer[] checkgroupIds);

    //分页查询
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    //通过id查套餐
    Setmeal findById(int id);

    //通过id查询选中的检查组ids
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    //修改更新套餐
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    //删除套餐
    void deleteById(int id) throws HealthException;

    //查出数据库中的所有图片
    List<String> findImgs();

    //查询所有
    List<Setmeal> findAll();

    //查询套餐详情方式一
    Setmeal findDetailById(int id);
    //方式二
    //Setmeal findDetailById2(int id);

    //套餐预约占比
    List<Map<String, Object>> getSetmealReport();

}
