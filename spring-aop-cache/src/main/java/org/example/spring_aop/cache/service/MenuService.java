package org.example.spring_aop.cache.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author lifei
 */
@Component
public class MenuService {
    @Cacheable(cacheNames = {"menu"})
    public List<String> getMenuList() {
        System.out.println("mock:get from db");
        return Arrays.asList("article", "comment", "admin");
    }
}
