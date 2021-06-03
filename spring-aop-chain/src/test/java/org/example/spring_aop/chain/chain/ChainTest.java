package org.example.spring_aop.chain.chain;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ChainTest {
    @Test
    public void test() {
        List<ChainHandler> chainHandlers = Arrays.asList(
                new ChainHandlerA(),
                new ChainHandlerB(),
                new ChainHandlerC()
        );
        Chain chain = new Chain(chainHandlers);
        chain.proceed();
    }
}
