package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * @Author: zengrui
 * @Date: 2020/9/18 21:23
 */
public interface CheckItemDao {
    /**
     * 查询 所有检查项
     * @return
     */
    List<CheckItem> findAll();

    //添加检查项
    void add(CheckItem checkItem);

    //分页条件查询
    Page<CheckItem> findByCondition(String queryString);

    //通过id查询
    CheckItem findById(int id);

    //更新修改检查项
    void update(CheckItem checkItem);

    // 检查 检查项是否被检查组使用了
    int findCountByCheckItemId(int id);

    //通过id删除检查项
    void deleteById(int id);


}
