package com.chengqj.study.eurekaclient1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chengqj
 * @Date 2020/12/1 17:52
 * @Desc
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        System.out.println("eureka-client1 port");
        return "Hello1 ~ port:8081";
    }

}
