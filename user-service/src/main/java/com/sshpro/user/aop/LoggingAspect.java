package com.sshpro.user.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j
public class LoggingAspect {

    @Before(value = "execution(* com.sshpro.user.controller.*.*(..))")
    public void logBefore(JoinPoint jp) {
        System.out.println("before");
        log.info("Before: " + jp.getSignature());
    }

    @After(value = "execution(* com.sshpro.user.controller.*.*(..))")
    public void logAfter(JoinPoint jp) {
        log.info("After: " + jp.getSignature());
    }

    @Around(value = "execution(* com.sshpro.user.service.*.*(..))")
    public Object logAround(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return jp.proceed();
        } catch (Exception e) {
            log.info(e.getMessage());
            throw e;
        } finally {
            log.info("Time taken: {} ms", (System.currentTimeMillis() - startTime));
        }
    }

} 
