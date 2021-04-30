package com.chengqj.study.gateway.config;

import com.chengqj.study.gateway.filterfactory.RequestTimeGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author chengqj
 * @Date 2020/12/19 15:11
 * @Desc 将过滤器工厂配置注册
 */
@Configuration
public class FilterFactoryConfig {
    @Bean
    public RequestTimeGatewayFilterFactory elapseGatewayFilterFactory() {
        return new RequestTimeGatewayFilterFactory();
    }
}
