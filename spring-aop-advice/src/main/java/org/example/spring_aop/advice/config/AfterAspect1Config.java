package org.example.spring_aop.advice.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author lifei
 */
@Aspect
@Component
public class AfterAspect1Config {
    @Pointcut("@annotation(org.example.spring_aop.advice.annotation.AdminOnly)")
    public void matchAnnotation() {
    }

    @After("matchAnnotation()")
    public void advice1() {
        System.out.println("###after matchAnnotation");
    }
}
