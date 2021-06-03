package org.example.spring_aop.advice.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author lifei
 */
@Aspect
@Component
public class BeforeAspectConfig {
    @Pointcut("execution(* *..*(Long))")
    public void matchLongArg() {
    }

    @Before("matchLongArg() && args(productId)")
    public void advice6(Long productId) {
        System.out.println("###before, arg = " + productId);
    }
}
