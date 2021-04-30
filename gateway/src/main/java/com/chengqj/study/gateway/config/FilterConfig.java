package com.chengqj.study.gateway.config;

import com.chengqj.study.gateway.selfdeffilter.RequestTimeFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author chengqj
 * @Date 2020/12/17 22:14
 * @Desc 注册自定义过滤器到路由
 */
@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes().route(r ->
                r.path("/hello/**") // 指定那种路由
                        .filters(f ->
                                f.filter(new RequestTimeFilter()) // 自定义过滤器
                                        .addRequestHeader("X-Response-Default-Foo", "Default-Bar") // 添加返回头过滤器
                        )
                        .uri("http://localhost:8080") // 要路由的uri
                        .order(1) // 优先级,越小越优先
                        .id("customer_filter_router")
        ).build();
    }


}
