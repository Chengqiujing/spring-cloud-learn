package com.chengqj.study.jwtuserservice.controller;

import com.chengqj.study.jwtuserservice.dto.UserLoginDTO;
import com.chengqj.study.jwtuserservice.entity.User;
import com.chengqj.study.jwtuserservice.service.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.controller
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/20 15:54
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceDetail userServiceDetail;

    @PostMapping("/register")
    public User postUser(@RequestParam("username") String username,@RequestParam("password") String password){
        return userServiceDetail.insertUser(username,password);
    }

    @PostMapping("/login")
    public UserLoginDTO login(@RequestParam("username")String username,@RequestParam("password") String password){
        return userServiceDetail.login(username,password);
    }


}
