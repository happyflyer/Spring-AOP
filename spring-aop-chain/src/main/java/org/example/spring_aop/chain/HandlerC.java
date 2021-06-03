package org.example.spring_aop.chain;

/**
 * @author lifei
 */
public class HandlerC extends Handler {
    @Override
    protected void handleProcess() {
        System.out.println("handle by C");
    }
}
