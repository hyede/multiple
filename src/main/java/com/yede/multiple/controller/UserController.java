package com.yede.multiple.controller;

import com.yede.multiple.mapper.UserInfoMapper;
import com.yede.multiple.model.User;
import com.yede.multiple.utils.DatabaseContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/users")
public class UserController {
    @Autowired
    private  UserInfoMapper userInfoMapper;

    //获取指定考试安排 指定学生的考试记录
    @RequestMapping(value="", method= RequestMethod.GET)
    public List<User> selectAll() {
        DatabaseContextHolder.setDataSourceKey("hzwitwin");
        return userInfoMapper.selectAll();
    }
}
