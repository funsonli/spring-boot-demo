package com.funsonli.springbootdemo111logaop.aop;

import com.funsonli.springbootdemo111logaop.annotation.BootanLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 日志aop
 *
 * @author Funson
 * @date 2018-06-11
 */

@Slf4j
@Aspect
@Component
public class BootanLogAspect {

    @Autowired(required = false)
    private HttpServletRequest request;

    @Pointcut("@annotation(com.funsonli.springbootdemo111logaop.annotation.BootanLog)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();

        try {
            result = point.proceed();
            long endTime = System.currentTimeMillis();

            insertLog(point, endTime - beginTime);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    private void insertLog(ProceedingJoinPoint point, long time) {
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();

        log.info(String.valueOf((int)(time)));

        BootanLog userAction = method.getAnnotation(BootanLog.class);
        if (userAction != null) {
            // 注解上的描述
            log.info(userAction.value());
            log.info(String.valueOf(userAction.type()));
        }

        Map<String, String[]> params = request.getParameterMap();
        log.info(request.getRequestURI());
        log.info(request.getMethod());

    }
}
