package org.example.spring_aop.proxy.jdk;

import org.junit.Test;

import java.lang.reflect.Proxy;

public class SubjectTest {
    @Test
    public void test() {
        // https://blog.csdn.net/MrYushiwen/article/details/111473126
        // System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        // System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        // -Djdk.proxy.ProxyGenerator.saveGeneratedFiles=true
        Subject subject = (Subject) Proxy.newProxyInstance(
                SubjectTest.class.getClassLoader(),
                new Class[]{Subject.class},
                new JdkProxySubject(new RealSubject())
        );
        subject.request();
        subject.hello();
    }
}
