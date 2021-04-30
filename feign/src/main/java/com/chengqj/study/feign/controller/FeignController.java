package com.chengqj.study.feign.controller;

import brave.Tracer;
import com.chengqj.study.feign.rpc.HelloCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chengqj
 * @Date 2020/12/2 11:46
 * @Desc
 */
@RestController
public class FeignController {
    // @Qualifier("helloCall")
    @Autowired
    HelloCall helloCall;

    @Autowired
    Tracer tracer;

    @GetMapping("/feign")
    // @HystrixCommand(fallbackMethod = "hiError")
    public String get() {
        tracer.currentSpan().tag("name", "btbullle");
        return helloCall.sayHello("这是Feign内容");
    }


    public String hiError() {
        return "hi,sorry,error!";
    }
}
