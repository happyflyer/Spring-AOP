package org.example.spring_aop.advice;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author lifei
 */
@SpringBootConfiguration
@ComponentScan("org.example.spring_aop.advice")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AdviceApplication {
}
