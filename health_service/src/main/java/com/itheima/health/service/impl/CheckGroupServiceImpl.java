package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: zengrui
 * @Date: 2020/9/21 9:44
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    //分页查询
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //有条件，要模糊查询
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            //拼接
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //条件查询
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());
        PageResult<CheckGroup> pageResult=new PageResult<CheckGroup>(page.getTotal(),page.getResult());
        return pageResult;
    }


    //添加检查组
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //添加检查组
        checkGroupDao.add(checkGroup);
        //获取检查组的id
        Integer checkGroupId = checkGroup.getId();
        //遍历检查项id，添加检查组与检查项的关系
        if (null!=checkitemIds){
            for (Integer checkitemId : checkitemIds) {
                //添加检查组与检查项的关系
                checkGroupDao.addCheckGroupCheckItem(checkGroupId,checkitemId);
            }
        }
    }

    //通过id获取检查组
    @Override
    public CheckGroup findById(int checkGroupId) {
        return checkGroupDao.findById(checkGroupId);
    }

    //通过检查组id查询选中的检查项id
    @Override
    public List<Integer> findCheckItemIdsByCheckGroup(int checkGroupId) {
        return checkGroupDao.findCheckItemIdsByCheckGroup(checkGroupId);
    }

    //修改检查组
    @Override
    public void update(CheckGroup checkGroup, Integer[] checkItemIds) {
        //先更新检查组
        checkGroupDao.update(checkGroup);
        //删除旧关系
        checkGroupDao.deleteCheckGroupCheckItem(checkGroup.getId());
        //建立新关系
        if (null!=checkItemIds){
            for (Integer checkItemId : checkItemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroup.getId(),checkItemId);
            }
        }

    }

    //删除检查组
    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
        //检查，这个检查组是否被套餐使用了
        int count= checkGroupDao.findSetmealCountByCheckGroupId(id);
        //被使用
        if (count>0){
            throw  new HealthException("访检查组已经被套餐使用了，不能删除");
        }
        //未被使用
        //先删除检查组与检查项关系
        checkGroupDao.deleteCheckGroupCheckItem(id);
        //在删除检查组
        checkGroupDao.deleteById(id);
    }

    //查询所有检查组
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
