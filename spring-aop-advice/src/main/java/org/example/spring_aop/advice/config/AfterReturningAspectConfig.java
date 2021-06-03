package org.example.spring_aop.advice.config;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author lifei
 */
@Aspect
@Component
public class AfterReturningAspectConfig {
    @Pointcut("execution(String *..*.*(..))")
    public void matchReturn() {
    }

    @AfterReturning(value = "matchReturn()", returning = "result")
    public void advice4(Object result) {
        System.out.println("###afterReturning matchReturn, result = " + result);
    }
}
