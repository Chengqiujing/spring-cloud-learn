package com.chengqj.study.feign.fallback;

import com.chengqj.study.feign.rpc.HelloCall;
import org.springframework.stereotype.Component;

/**
 * @Author chengqj
 * @Date 2020/12/7 14:49
 * @Desc
 */
@Component("hystrixFallback")
public class HystrixFallback implements HelloCall {
    @Override
    public String sayHello(String name) {
        return "Hystrix fallback: ~~~";
    }
}
