package org.example.spring_aop.aspect.annotation;

import java.lang.annotation.*;

/**
 * @author lifei
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NeedSecured {
}
