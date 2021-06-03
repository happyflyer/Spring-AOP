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
public class PackageAspectConfig {
    @Pointcut("within(org.example.spring_aop.aspect.service.ProductService)")
    public void matchPackage() {
    }

    @Before("matchPackage()")
    public void before() {
        System.out.println("###before matchPackage");
    }
}
