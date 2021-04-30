package com.chengqj.study.authservicehi.controller;

import com.chengqj.study.authservicehi.entity.User;
import com.chengqj.study.authservicehi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.controller
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 22:39
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registry", method = RequestMethod.POST)
    public User createUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userService.create(user);
    }
}
