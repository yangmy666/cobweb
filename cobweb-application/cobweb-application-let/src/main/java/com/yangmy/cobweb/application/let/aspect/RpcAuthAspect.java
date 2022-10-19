//package com.yangmy.cobweb.application.let.aspect;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
///**
// * @author YangMingYang
// * @since 2022/10/19
// */
//@Slf4j
//@Component
//@Aspect
//public class RpcAuthAspect {
//
//    @Pointcut("execution(* com.yangmy.cobweb.application.let.controller.*.*(..))")
//    private void pointcut(){}
//
//    @Around("pointcut()")
//    public Object around(ProceedingJoinPoint pjp) throws Throwable {
//
//        return pjp.proceed();
//    }
//}
