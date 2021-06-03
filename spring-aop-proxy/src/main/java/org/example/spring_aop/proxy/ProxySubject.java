package org.example.spring_aop.proxy;

/**
 * @author lifei
 */
public class ProxySubject implements Subject {
    private final Subject realSubject;

    public ProxySubject(Subject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public void request() {
        System.out.println("before");
        try {
            this.realSubject.request();
        } catch (Exception e) {
            System.out.println("ex: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("finally");
        }
    }
}
