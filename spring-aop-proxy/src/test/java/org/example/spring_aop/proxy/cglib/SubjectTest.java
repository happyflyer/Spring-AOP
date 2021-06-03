package org.example.spring_aop.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import org.junit.Test;

public class SubjectTest {
    @Test
    public void test() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealSubject.class);
        enhancer.setCallback(new DemoMethodInterceptor());
        Subject subject = (Subject) enhancer.create();
        subject.request();
        subject.hello();
    }
}
