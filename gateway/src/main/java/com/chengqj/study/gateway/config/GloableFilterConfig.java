package com.chengqj.study.gateway.config;

import com.chengqj.study.gateway.gloablefilter.SelfTokenGloableFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author chengqj
 * @Date 2020/12/20 14:01
 * @Desc
 */
@Configuration
public class GloableFilterConfig {
    @Bean
    public SelfTokenGloableFilter tokenGloableFilter() {
        return new SelfTokenGloableFilter();
    }
}
