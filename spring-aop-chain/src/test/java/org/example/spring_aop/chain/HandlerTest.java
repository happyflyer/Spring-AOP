package org.example.spring_aop.chain;

import org.junit.Test;

public class HandlerTest {
    @Test
    public void test() {
        Handler handlerA = new HandlerA();
        Handler handlerB = new HandlerB();
        Handler handlerC = new HandlerC();
        handlerA.setSuccessorHandler(handlerB);
        handlerB.setSuccessorHandler(handlerC);
        handlerA.execute();
    }
}
