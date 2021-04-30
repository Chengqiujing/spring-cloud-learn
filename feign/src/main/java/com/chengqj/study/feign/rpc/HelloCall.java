package com.chengqj.study.feign.rpc;


import com.chengqj.study.feign.config.FeignConfig;
import com.chengqj.study.feign.fallback.HystrixFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author chengqj
 * @Date 2020/12/2 11:04
 * @Desc
 */
@FeignClient(value = "eureka-client", configuration = FeignConfig.class, fallback = HystrixFallback.class)
public interface HelloCall {
    @GetMapping("/hello")
    String sayHello(@RequestParam("name") String name);

}
