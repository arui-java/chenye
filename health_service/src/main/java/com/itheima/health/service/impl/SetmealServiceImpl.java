package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: zengrui
 * @Date: 2020/9/21 21:32
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    //添加套餐
    @Override
    @Transactional
    public Integer add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐信息
        setmealDao.add(setmeal);
        //获取新增套餐的id
        Integer setmealId = setmeal.getId();
        //遍历选中的检查组id，添加套餐与检查组的关系
        if (null!=checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId,checkgroupId);
            }
        }
        return setmealId;
    }

    //分页查询
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //查询条件
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())){
            //模糊查询
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        //条件查询，语句会被分页
        Page<Setmeal> page= setmealDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<Setmeal>(page.getTotal(),page.getResult());
    }

    //通过id查套餐
    @Override
    public Setmeal findById(int id) {
        return setmealDao.finById(id);
    }

    //通过id查询选中的检查组ids
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    //修改更新套餐
    @Override
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        //更新套餐信息
        setmealDao.update(setmeal);
        //删除旧关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        //添加新关系
        if (null!=checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(),checkgroupId);
            }
        }
    }

    //删除套餐
    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
        //判断是否被订单使用，使用不能删除
        int count= setmealDao.findOrderCountBySetmealId(id);
        if (count>0){
            throw new HealthException("该套餐被订单使用，不能删除！");
        }
        //删除套餐与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);
        //删除套餐
        setmealDao.deleteById(id);
    }

    //查出数据库中的所有图片
    @Override
    public List<String> findImgs() {
        return setmealDao.findImgs();
    }

    //查询所有
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    //查询套餐详情方式一
    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }
    //方式二
   /* @Override
    public Setmeal findDetailById2(int id) {
        return setmealDao.findDetailById2(id);
    }*/

}
