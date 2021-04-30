package com.chengqj.study.ribbon.controller;

import com.chengqj.study.ribbon.service.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author chengqj
 * @Date 2020/12/1 18:10
 * @Desc
 */
@RestController
public class RibbonController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    /**
     * 第一种方式 通过@LoadBalanced注解
     * @return
     */
    // @GetMapping("/ribbon")
    // public String test(){
    //     String url = "http://eureka-client/hello";
    //     String result = restTemplate.getForObject(url, String.class);
    //     return result;
    // }
    //
    // /**
    //  * 第二种方式 通过LoadLoadBalancerClient对象
    //  * @return
    //  */
    // @GetMapping("/ribbon2")
    // public String test2(){
    //     ServiceInstance choose = loadBalancerClient.choose("eureka-client");
    //     String url = "http://"+choose.getHost()+":"+choose.getPort()+"/hello";
    //     System.out.println(url);
    //     String result = restTemplate.getForObject(url, String.class);
    //     return result;
    // }

    /**
     * 自定义服务列表
     */
    @GetMapping("/ribbon3")
    public String test3() {
        ServiceInstance choose = loadBalancerClient.choose("stores");
        String result = restTemplate.getForObject(choose.getUri(), String.class);
        return result;
    }

    @Autowired
    RibbonService ribbonService;

    /**
     * Hystrix Demo
     */
    @GetMapping("/hystrixDemo")
    // @HystrixCommand(fallbackMethod = "hiError")
    public String test4(String name) {
        if (name == null) {
            name = "无名氏";
        }
        return ribbonService.hi(name);
    }
    // public String hiError(String name) {
    //     if (name == null) {
    //         name = "";
    //     }
    //     return "hi," + name + ",sorry,error!";
    // }
}
