package com.auth_service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(public * com.auth_service.service.*.*(..))")
    public void serviceLayer(){}

    @Pointcut("execution(public * com.auth_service.security..*.*(..))")
    public void securityLayer(){}

    @Pointcut("execution(public * com.auth_service.controller.*.*(..))")
    public void controllerLayer(){}

    @Around("serviceLayer() || securityLayer() || controllerLayer()")
    public Object logCombinedExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        String signature = joinPoint.getSignature().toShortString();
        long startTime = System.currentTimeMillis();

        log.info("AOP: method entrance: {}", signature);

        try{

            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("AOP: method exit: {}. Method executed in {} ms.", signature, duration);

            return result;
        } catch (Throwable e) {

            log.error("AOP: exception in {}:{}", signature, e.getMessage(), e);
            throw e;
        }


    }
}
