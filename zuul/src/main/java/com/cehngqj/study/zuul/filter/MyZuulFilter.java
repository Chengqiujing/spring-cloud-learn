package com.cehngqj.study.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author chengqj
 * @Date 2020/12/8 14:32
 * @Desc
 */
@Component
public class MyZuulFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(MyZuulFilter.class);

    private static final String PRE_TYPE = "pre"; // 路由前
    private static final String POST_TYPE = "post"; // 路由后
    private static final String ROUTING_TYPE = "routing"; // 路由中
    private static final String ERROR_TYPE = "error"; // 其他过滤器报错

    @Override
    public String filterType() { // 指定过滤器是那种类型
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() { // 指定过滤器的优先级, 数字越小越优先
        return 0;
    }

    @Override
    public boolean shouldFilter() { // 指定过滤器是否生效
        return true;
    }

    @Override
    public Object run() throws ZuulException { // 校验逻辑
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getParameter("token");
        if (token == null) {
            log.warn("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);

            try {
                ctx.getResponse().getWriter().write("token is empty");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        log.info("ok");
        return null;
    }
}
