package com.chengqj.study.eurekaclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author chengqj
 * @Date 2020/12/1 17:52
 * @Desc
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(String name, HttpServletRequest request) {
        System.out.println("eureka-client");
        if (name != null) {
            String header = request.getHeader(name);
            System.out.println("header: " + header);
            System.out.println("name: " + name);
        }
        return "Hello ~ port:8080";
    }

}
