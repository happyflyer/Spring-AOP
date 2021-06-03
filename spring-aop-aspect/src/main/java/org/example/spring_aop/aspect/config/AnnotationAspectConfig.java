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
public class AnnotationAspectConfig {
    @Pointcut("@args(org.example.spring_aop.aspect.annotation.NeedSecured)")
    public void matchAnnotation() {
    }

    @Before("matchAnnotation()")
    public void before() {
        System.out.println("###before matchAnnotation");
    }
}
