package org.example.spring_aop.chain.chain;

/**
 * @author lifei
 */
public class ChainHandlerA extends ChainHandler {
    @Override
    protected void handleProcess() {
        System.out.println("handle by A");
    }
}
