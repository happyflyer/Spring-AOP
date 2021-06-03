package org.example.spring_aop.advice.log;

import org.springframework.stereotype.Component;

/**
 * @author lifei
 */
@Component
public class LogService {
    public String log(Long id) throws IllegalAccessException {
        Long exceptionId = 2L;
        System.out.println("log from LogService.log");
        if (id.equals(exceptionId)) {
            return "log content: " + id;
        } else {
            throw new IllegalAccessException("exception in LogService.log");
        }
    }
}
