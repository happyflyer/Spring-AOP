package org.example.spring_aop.teach;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author lifei
 */
@SpringBootConfiguration
@ComponentScan("org.example.spring_aop.teach")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TeachApplication {
}
