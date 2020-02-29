package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void ponintCut() {

    }

    @Before("ponintCut()")
    public void before(){
        System.out.println("before");
    }

    @After("ponintCut()")
    public void after(){
        System.out.println("after");
    }

    @AfterReturning("ponintCut()")
    public void afterReturning(){
        System.out.println("afterReturning");
    }

    @Around("ponintCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object obj = joinPoint.proceed();
        System.out.println("around after");
        return obj;
    }
}
