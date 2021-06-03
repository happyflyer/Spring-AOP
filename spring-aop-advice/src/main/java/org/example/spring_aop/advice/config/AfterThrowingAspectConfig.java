package org.example.spring_aop.advice.config;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author lifei
 */
@Aspect
@Component
public class AfterThrowingAspectConfig {
    @Pointcut("execution(* *..*(*) throws java.lang.IllegalAccessException)")
    public void matchException() {
    }

    @AfterThrowing("matchException()")
    public void advice3() {
        System.out.println("###afterThrowing matchException");
    }
}
