package com.chengqj.study.gateway.selfdeffilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author chengqj
 * @Date 2020/12/17 21:52
 * @Desc 自定义过滤器-计算请求时间
 */
public class RequestTimeFilter implements GatewayFilter, Ordered {
    private static final Log log = LogFactory.getLog(GatewayFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 相当于pre方法 -- 开始
        exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
        // pre结束

        return chain.filter(exchange).then(Mono.fromRunnable(() -> { // 相当于post方法 -- 开始
            Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
            if (startTime != null) {
                log.info(exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms--源自过滤器");
            }
        }));
        // 结束
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
