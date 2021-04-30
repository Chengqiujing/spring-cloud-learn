package com.chengqj.study.consulconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.util.List;

@SpringBootTest
class ConsulConsumerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private LoadBalancerClient loadBalancer;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    public void test() {

        String s = loadBalancer.choose("consul-cons").getUri().toString();

        System.out.println(s);
    }

    @Test
    public void test2() {

        List<ServiceInstance> instances = discoveryClient.getInstances("consul-cons");

        System.out.println(instances);
    }
}
