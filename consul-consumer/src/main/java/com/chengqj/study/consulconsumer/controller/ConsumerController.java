package com.chengqj.study.consulconsumer.controller;

import com.chengqj.study.consulconsumer.service.HiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chengqj
 * @Date 2020/12/27 16:11
 * @Desc
 */
@RestController
public class ConsumerController {
    @Autowired
    HiService hiService;

    @Autowired
    private LoadBalancerClient loadBalancer;
    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/call")
    public String call(String name) {
        return hiService.sayHi(name);
    }

    @RequestMapping("/call1")
    public String call1(String name) {

        return hiService.sayHi(name);
    }
}
