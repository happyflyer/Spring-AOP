package org.example.spring_aop.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author lifei
 */
public class DemoMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects,
                            MethodProxy methodProxy)
            throws Throwable {
        Object result;
        System.out.println("before in cglib");
        try {
            result = methodProxy.invokeSuper(o, objects);
        } catch (Exception e) {
            System.out.println("ex: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("finally in cglib");
        }
        return result;
    }
}
