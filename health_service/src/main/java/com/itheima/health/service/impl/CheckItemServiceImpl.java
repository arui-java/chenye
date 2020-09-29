package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: zengrui
 * @Date: 2020/9/18 21:19
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
     private CheckItemDao checkItemDao;
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    //添加检查项
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //分页查询
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //Mapper接口方式的调用，推荐这种使用方式
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //判断是否有查询条件
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())){
            //有查询条件，拼接%
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        //查询语句会被分页
        Page<CheckItem> page=checkItemDao.findByCondition(queryPageBean.getQueryString());
        //防止数据丢失，Page应用的是基础数据类型，没有实现序列化
        // 封装分页到结果对象中
        PageResult<CheckItem> pageResult=new PageResult<CheckItem>(page.getTotal(),page.getResult());
        return pageResult;
    }

    //通过id查询
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    //更新修改数据
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    //删除
    @Override
    public void deleteById(int id) throws HealthException {
        //先判断这个检查项是否被检查组使用了
        //调用dao查询检查项的id是否在t_checkgroup_checkitem表中存在记录
        int cnt= checkItemDao.findCountByCheckItemId(id);

        //被使用了就不能删除
        if (cnt>0){
            //已经被检查组使用，则不能删除，报错
            throw new HealthException("该检查项被检查组使用了，不能删除");
        }
        //没被用，就可以删除
        checkItemDao.deleteById(id);
    }
}
