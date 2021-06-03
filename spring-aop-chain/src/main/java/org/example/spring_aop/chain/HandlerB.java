package org.example.spring_aop.chain;

/**
 * @author lifei
 */
public class HandlerB extends Handler {
    @Override
    protected void handleProcess() {
        System.out.println("handle by B");
    }
}
