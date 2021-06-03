package org.example.spring_aop.advice;

import org.example.spring_aop.advice.log.LogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogServiceTest {
    @Autowired
    LogService logService;

    @Test
    public void testExecution() {
        String result = null;
        try {
            result = logService.log(1L);
            System.out.println("result = " + result);
        } catch (IllegalAccessException ignored) {
        }
        System.out.println("==========");
        try {
            result = logService.log(2L);
            System.out.println("result = " + result);
        } catch (IllegalAccessException ignored) {
        }
    }
}
