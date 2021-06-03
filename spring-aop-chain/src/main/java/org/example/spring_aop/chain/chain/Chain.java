package org.example.spring_aop.chain.chain;

import java.util.List;

/**
 * @author lifei
 */
public class Chain {
    private final List<ChainHandler> chainHandlers;
    private int index = 0;

    public Chain(List<ChainHandler> chainHandlers) {
        this.chainHandlers = chainHandlers;
    }

    public void proceed() {
        if (index >= this.chainHandlers.size()) {
            return;
        }
        chainHandlers.get(index++).execute(this);
    }
}
