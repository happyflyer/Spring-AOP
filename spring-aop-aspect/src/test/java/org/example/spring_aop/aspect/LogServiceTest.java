package org.example.spring_aop.aspect;

import org.example.spring_aop.aspect.log.LogService;
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
        logService.log();
    }
}
