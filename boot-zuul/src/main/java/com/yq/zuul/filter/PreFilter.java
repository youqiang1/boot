package com.yq.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> 请求过滤器，所有的资源请求在路由之前进行前置过滤(pre)</p>
 * @author youq  2019/4/24 19:09
 */
@Slf4j
@Component
public class PreFilter extends ZuulFilter {

    /**
     * <p>
     * 四种类型：pre,routing,error,post
     * pre：主要用在路由映射的阶段是寻找路由映射表的
     * routing:具体的路由转发过滤器是在routing路由器，具体的请求转发的时候会调用
     * error:一旦前面的过滤器出错了，会调用error过滤器。
     * post:当routing，error运行完后才会调用该过滤器，是在最后阶段的
     * </p>
     * @return java.lang.String
     * @author youq  2019/4/24 19:10
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * <p> 自定义过滤器执行的顺序，数值越大越靠后执行，越小就越先执行</p>
     * @return int
     * @author youq  2019/4/24 19:13
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * <p> 控制过滤器生效不生效，可以在里面写一串逻辑来控制</p>
     * @return boolean
     * @author youq  2019/4/24 19:14
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * <p> 执行过滤逻辑</p>
     * @return java.lang.Object
     * @author youq  2019/4/24 19:14
     */
    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        //请求路径放行使用
        String requestURL = request.getRequestURL().toString();
        String remoteHost = request.getRemoteHost();
        log.info("requestURL:{}, remoteHost:{}", requestURL, remoteHost);
        //是否携带指定的请求参数，也可以在请求头中指定
        // String token = request.getParameter("token");
        // if (StringUtils.isEmpty(token)) {
        //     context.setSendZuulResponse(false);
        //     context.setResponseStatusCode(401);
        //     context.setResponseBody("unAuthorized");
        //     return null;
        // }
        return null;
    }
}
