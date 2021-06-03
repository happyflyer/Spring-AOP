package org.example.spring_aop.aspect.service.sub;

import org.springframework.stereotype.Service;

/**
 * @author lifei
 */
@Service
public class SubService {
    public void demo() {
        System.out.println("execute SubService.demo()\n");
    }
}
