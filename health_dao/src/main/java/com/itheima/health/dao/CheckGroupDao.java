package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zengrui
 * @Date: 2020/9/21 9:44
 */
public interface CheckGroupDao {

    //添加检查组
    void add(CheckGroup checkGroup);
    //添加检查组与检查项的关系
    //@param 注意要取别名，类型相同
    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId,@Param("checkitemId") Integer checkitemId);

    //分页查询
    Page<CheckGroup> findPage(String queryString);

    //通过id获取检查组
    CheckGroup findById(int checkGroupId);

    //通过检查组id查询选中的检查项id
    List<Integer> findCheckItemIdsByCheckGroup(int checkGroupId);

    //更新检查组
    void update(CheckGroup checkGroup);

    //删除检查组与检查项的关系
    void deleteCheckGroupCheckItem(Integer id);

    //通过检查组id查询是否被套餐使用了
    int findSetmealCountByCheckGroupId(int id);

    //删除检查组
    void deleteById(int id);

    //查询所有检查组
    List<CheckGroup> findAll();
}
