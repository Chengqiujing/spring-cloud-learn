package com.chengqj.study.consulconsumer.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @Author chengqj
 * @Date 2020/12/23 22:07
 * @Desc
 */
@FeignClient(value = "consul-prov")
public interface HiFeignInterface {
    @GetMapping("/hi")
    String sayHiFromClient(@RequestParam(value = "name") String name);
}
