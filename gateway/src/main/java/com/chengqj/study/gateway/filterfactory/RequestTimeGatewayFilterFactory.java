package com.chengqj.study.gateway.filterfactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @Author chengqj
 * @Date 2020/12/19 14:38
 * @Desc 自定义过滤器工厂, 还需注册工厂才能使用
 *  计算请求时间
 */
public class RequestTimeGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestTimeGatewayFilterFactory.Config> {
    private static final Log log = LogFactory.getLog(GatewayFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";
    private static final String KEY = "withParams";

    public RequestTimeGatewayFilterFactory() {
        super(Config.class);  // 这个super必须写
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY); // 指定参数列表? 不知道什么作用,暂时标记
    }

    @Override
    public GatewayFilter apply(Config config) {  // 过滤器工厂的方法
        return (exchange, chain) -> {
            exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                        if (startTime != null) {
                            StringBuilder bs = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                                    .append(": ")
                                    .append(System.currentTimeMillis() - startTime)
                                    .append("ms--原则过滤器工厂");
                            if (config.isWithParams()) {
                                bs.append(" params:")
                                        .append(exchange.getRequest().getQueryParams());
                            }
                            log.info(bs.toString());
                        }
                    })
            );
        };
    }

    /**
     * 这个内部类是用于重写public GatewayFilter apply(Config config)时,接收参数用, 这和泛型中指定的类型有关系
     */
    public static class Config {
        private boolean withParams;

        public boolean isWithParams() {
            return withParams;
        }

        public void setWithParams(boolean withParams) {
            this.withParams = withParams;
        }
    }
}
