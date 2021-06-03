package org.example.spring_aop.chain.chain;

/**
 * @author lifei
 */
public abstract class ChainHandler {
    public void execute(Chain chain) {
        this.handleProcess();
        chain.proceed();
    }

    /**
     * handleProcess
     */
    protected abstract void handleProcess();
}
