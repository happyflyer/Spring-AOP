package org.example.spring_aop.chain.chain;

/**
 * @author lifei
 */
public class ChainHandlerC extends ChainHandler {
    @Override
    protected void handleProcess() {
        System.out.println("handle by C");
    }
}
