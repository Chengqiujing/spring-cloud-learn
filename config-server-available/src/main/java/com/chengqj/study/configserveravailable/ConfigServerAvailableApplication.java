package com.chengqj.study.configserveravailable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class ConfigServerAvailableApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerAvailableApplication.class, args);
    }

}
