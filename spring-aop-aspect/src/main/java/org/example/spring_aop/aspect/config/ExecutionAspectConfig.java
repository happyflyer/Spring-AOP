package org.example.spring_aop.aspect.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author lifei
 */
@Aspect
@Component
public class ExecutionAspectConfig {
    @Pointcut("execution(public * org.example.spring_aop.aspect.service.*Service.*(..))")
    public void matchCondition() {
    }

    @Before("matchCondition()")
    public void before() {
        System.out.println("###before matchCondition");
    }
}
