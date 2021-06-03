package org.example.spring_aop.advice.service;

import org.example.spring_aop.advice.annotation.AdminOnly;
import org.springframework.stereotype.Service;

/**
 * @author lifei
 */
@Service
public class ProductService {
    public String findProductNameById(Long id) {
        System.out.println("execute getProductNameById(id=" + id + ")");
        return "product name";
    }

    public void exceptionMethod(String arg) throws IllegalAccessException {
        System.out.println("execute exceptionMethod(arg=" + arg + ")");
        throw new IllegalAccessException("one exception");
    }

    @AdminOnly
    public void addProduct(Long id, String name) {
        System.out.println("execute addProduct(id=" + id + ", name=" + name + ")");
    }

    @AdminOnly
    public void deleteProduct(Long id) {
        System.out.println("execute deleteProduct(id=" + id + ")");
    }

    public void loopUpProduct(Long id) {
        System.out.println("execute loopUpProduct(id=" + id + ")");
    }
}
