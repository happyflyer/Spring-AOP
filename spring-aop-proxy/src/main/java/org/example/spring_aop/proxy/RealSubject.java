package org.example.spring_aop.proxy;

/**
 * @author lifei
 */
public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("real subject execute request");
    }
}
