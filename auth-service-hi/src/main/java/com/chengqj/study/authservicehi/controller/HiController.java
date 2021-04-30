package com.chengqj.study.authservicehi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * <p>
 * 这是一个测试类
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.controller
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 22:46
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@RestController
@Slf4j
public class HiController {

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String home() {
        return "hi, i am from port: " + port;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/hello")
    public String hello() {
        return "hello you!";
    }

    @GetMapping("/getPrinciple")
    public OAuth2Authentication getPrinciple(OAuth2Authentication oauth2authentication,
                                             Principal principal, Authentication authentication) {
        log.info(oauth2authentication.toString());
        log.info("principal.toString()" + principal.toString());
        log.info("principal.getName()" + principal.getName());
        log.info("authentication:" + authentication.getAuthorities().toString());
        return oauth2authentication;
    }
}
