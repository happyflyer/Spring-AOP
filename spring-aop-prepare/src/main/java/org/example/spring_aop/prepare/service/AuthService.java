package org.example.spring_aop.prepare.service;

import org.example.spring_aop.prepare.security.CurrentUserHolder;
import org.springframework.stereotype.Service;

/**
 * @author lifei
 */
@Service
public class AuthService {
    public void checkAccess() {
        System.out.println("执行了checkAccess...");
        String user = CurrentUserHolder.get();
        if (!"admin".equals(user)) {
            throw new RuntimeException("operation not allow");
        }
    }
}
