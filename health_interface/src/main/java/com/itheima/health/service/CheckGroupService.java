package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

/**
 * @Author: zengrui
 * @Date: 2020/9/21 9:43
 */
public interface CheckGroupService {
    //分页查询
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    //添加检查组
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    //通过id获取检查组
    CheckGroup findById(int checkGroupId);

    //通过检查组id查询选中的检查项id
    List<Integer> findCheckItemIdsByCheckGroup(int checkGroupId);

    //修改检查组
    void update(CheckGroup checkGroup, Integer[] checkItemIds);

    //删除检查组
    //【注意】这里的异常是我们自己抛出的异常类，不要导错包了(HandlerException是错的)
    void deleteById(int id) throws HealthException;

    //查询所有检查组
    List<CheckGroup> findAll();
}
