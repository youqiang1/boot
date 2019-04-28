package com.yq.limit.aspect;

import com.yq.limit.anno.RateLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * <p> 限流拦截器</p>
 * @author youq  2019/4/28 15:24
 */
@Slf4j
@Aspect
@Configuration
public class LimitAspect {

    @Autowired
    private RedisTemplate<String, Serializable> limitRedisTemplate;

    @Autowired
    private DefaultRedisScript<Number> redisLuaScript;

    @Around("execution(* com.yq.limit.controller ..*(..))")
    public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        if (rateLimit != null) {
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ipAddress = getIpAddress(request);
            String sb = ipAddress + "-" + targetClass.getName() + "-" + method.getName() + "-" + rateLimit.key();
            List<String> keys = Collections.singletonList(sb);
            Number number = limitRedisTemplate.execute(redisLuaScript, keys, rateLimit.count(), rateLimit.time());
            if (number != null && number.intValue() != 0 && number.intValue() < rateLimit.count()) {
                log.info("限流时间段内访问第【{}】次", number.toString());
                return joinPoint.proceed();
            }
        } else {
            return joinPoint.proceed();
        }
        throw new RuntimeException("已经到设置的限流次数了，禁止访问");
    }

    /**
     * <p> 获取请求的ip地址</p>
     * @param request HttpServletRequest
     * @return java.lang.String
     * @author youq  2019/4/28 15:37
     */
    private String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length()
            if (ipAddress != null && ipAddress.length() > 15) {
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

}
