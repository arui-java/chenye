package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengrui
 * @Date: 2020/9/21 22:03
 */
public interface SetmealDao {
    //添加套餐信息
    void add(Setmeal setmeal);

    //添加套餐与检查组的关系
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId,@Param("checkgroupId") Integer checkgroupId);

    //分页查询
    Page<Setmeal> findByCondition(String queryString);

    //通过id查套餐
    Setmeal finById(int id);

    //通过id查询选中的检查组ids
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    //修改更新套餐信息
    void update(Setmeal setmeal);

    //删除旧关系
    void deleteSetmealCheckGroup(Integer id);

    //通过套餐的id查询使用了这个套餐的订单个数
    int findOrderCountBySetmealId(int id);

    //删除套餐
    void deleteById(int id);

    //查询数据库中套餐的所有图片
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
