package com.chengqj.study.authservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.authservice.controller
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/12 14:16
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return principal;
    }
}
