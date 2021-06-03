package org.example.spring_aop.teach.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.spring_aop.teach.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lifei
 */
@Aspect
@Component
public class SecurityAspect {
    @Autowired
    AuthService authService;

    @Pointcut("@annotation(AdminOnly)")
    public void adminOnly() {
    }

    @Before("adminOnly()")
    public void check() {
        authService.checkAccess();
    }
}
