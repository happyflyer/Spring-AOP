package org.example.spring_aop.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lifei
 */
public class JdkProxySubject implements InvocationHandler {
    private final Subject realSubject;

    public JdkProxySubject(Subject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object result;
        System.out.println("before");
        try {
            result = method.invoke(realSubject, args);
        } catch (Exception e) {
            System.out.println("ex: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("finally");
        }
        return result;
    }
}
