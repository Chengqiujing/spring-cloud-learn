package com.chengqj.study.adminclientcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AdminClientCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminClientCloudApplication.class, args);
    }

}
