package org.example.spring_aop.advice.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author lifei
 */
@Aspect
@Component
public class AroundAspectConfig {
    @Pointcut("execution(* *..*(Long))")
    public void matchLongArg() {
    }

    @Around("matchLongArg()")
    public Object advice5(ProceedingJoinPoint joinPoint) {
        System.out.println("###around before");
        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
            System.out.println("###around after returning, result = " + result);
        } catch (Throwable throwable) {
            System.out.println("###around after throwing");
        } finally {
            System.out.println("###around finally");
        }
        return result;
    }
}
