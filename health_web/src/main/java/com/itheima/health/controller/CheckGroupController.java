package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zengrui
 * @Date: 2020/9/21 9:34
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    //添加检查组
    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        //调用业务层
        checkGroupService.add(checkGroup,checkitemIds);
        //响应结果
        return  new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用服务分页查询
        PageResult<CheckGroup> pageResult=checkGroupService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }

    //通过id获取检查组
    @GetMapping("/findById")
    public Result findById(int checkGroupId){
        //调用业务服务
        CheckGroup checkGroup= checkGroupService.findById(checkGroupId);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }

    //通过检查组id查询选中的检查项id
    @GetMapping("/findCheckItemIdsByCheckGroup")
    public Result findCheckItemIdsByCheckGroup(int checkGroupId){
       List<Integer> checkItemIds= checkGroupService.findCheckItemIdsByCheckGroup(checkGroupId);
       return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItemIds);
    }

    //修改检查组
    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        checkGroupService.update(checkGroup,checkitemIds);
        //响应结果
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    //删除检查组
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        checkGroupService.deleteById(id);
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //查询所有
    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> list= checkGroupService.findAll();
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
    }
}
