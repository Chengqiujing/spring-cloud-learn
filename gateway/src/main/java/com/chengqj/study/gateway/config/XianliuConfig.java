package com.chengqj.study.gateway.config;

import com.chengqj.study.gateway.xianliu.HostAddrKeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author chengqj
 * @Date 2020/12/20 17:36
 * @Desc 将限流类进行注册
 */
@Configuration
public class XianliuConfig {
    @Bean
    public HostAddrKeyResolver hostAddrKeyResolver() {
        return new HostAddrKeyResolver();
    }
}
