package org.example.spring_aop.aspect.log;

import org.springframework.stereotype.Component;

/**
 * @author lifei
 */
@Component
public class LogService implements Loggable {
    @Override
    public void log() {
        System.out.println("log from LogService");
    }
}
