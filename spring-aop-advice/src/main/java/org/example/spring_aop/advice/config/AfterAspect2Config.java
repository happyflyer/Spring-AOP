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
public class AfterAspect2Config {
    @Pointcut("execution(* *..*(Long))")
    public void matchLongArg() {
    }

    @After("matchLongArg()")
    public void advice2() {
        System.out.println("###after matchLongArg");
    }
}
