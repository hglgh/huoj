package com.hgl.huojbackend.common.request;


import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName RequestAspect
 * @Author 请别把我整破防
 * @Description //TODO
 * @Date 2025/7/8 16:02
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class RequestAspect {
    @Around("""
            @within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller)
            """)
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        log.info("↓↓↓↓↓↓↓↓↓↓ 请求日志 ↓↓↓↓↓↓↓↓↓↓");
        log.info("请求方式:[{}]，请求地址：[{}]", request.getMethod(), request.getRequestURI());
        log.info("请求方法: {}.{}", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
        log.info("请求参数: {}", JSONUtil.toJsonStr(filterArgs(joinPoint.getArgs())));
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("接口耗时: {} ms", end - start);
        log.info("↑↑↑↑↑↑↑↑↑↑ 请求日志 ↑↑↑↑↑↑↑↑↑↑");
        return result;
    }

    private List<Object> filterArgs(Object[] args) {
        return Arrays.stream(args).filter(arg ->
                !(arg instanceof HttpServletRequest)
                        && !(arg instanceof HttpServletResponse)
                        && !(arg instanceof MultipartFile)
        ).toList();
    }
}
