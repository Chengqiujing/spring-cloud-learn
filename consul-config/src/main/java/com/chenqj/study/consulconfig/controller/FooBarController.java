
package com.chenqj.study.consulconfig.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chengqj
 * @Date 2021/1/1 10:57
 * @Desc
 */
@RestController
@RefreshScope
public class FooBarController {
    @Value("${foo.say}")
    private String fooBar;

    @GetMapping("/foo")
    public String getFooBar() {
        return fooBar;
    }

}
