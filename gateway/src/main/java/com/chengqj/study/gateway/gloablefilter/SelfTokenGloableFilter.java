package com.chengqj.study.gateway.gloablefilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @Author chengqj
 * @Date 2020/12/20 0:17
 * @Desc 自定义全局过滤器, 全局处理器不需要配置,作用于所有
 * 实现过滤有token的就转发
 */
public class SelfTokenGloableFilter implements GlobalFilter, Ordered {
    Logger logger = LoggerFactory.getLogger(SelfTokenGloableFilter.class);  // 日志类

    // 全局过滤器要实现的方法
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // pre方法
        String token = exchange.getRequest().getQueryParams().getFirst("token"); // 获取token
        if (Objects.isNull(token) || token.isEmpty()) { // 如果tocken为空,则拒绝请求
            logger.info("token is empty");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange); // 如果token不为空,则继续
    }

    @Override
    public int getOrder() { // 请求优先级, 越小越优先
        return -100;
    }
}
