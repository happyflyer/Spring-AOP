package org.example.spring_aop.proxy;

import org.junit.Test;

public class SubjectTest {
    @Test
    public void test() {
        Subject subject = new ProxySubject(new RealSubject());
        subject.request();
    }
}
