package org.example.spring_aop.aspect.service;

import org.example.spring_aop.aspect.annotation.AdminOnly;
import org.example.spring_aop.aspect.annotation.NeedSecured;
import org.example.spring_aop.aspect.domain.Product;
import org.example.spring_aop.aspect.log.Loggable;
import org.springframework.stereotype.Service;

/**
 * @author lifei
 */
@Service
@NeedSecured
public class ProductService implements Loggable {
    public String getName() {
        System.out.println("execute ProductService.getName()\n");
        return "product service";
    }

    public void exDemo() throws IllegalAccessException {
        System.out.println("execute ProductService.exDemo()\n");
    }

    public void findById(Long id) {
        System.out.println("execute ProductService.findById(id=" + id + ")\n");
    }

    public void findByTwoArgs(Long id, String name) {
        System.out.println("execute ProductService.findByTwoArgs(id=" + id + ", name=" + name + ")\n");
    }

    @Override
    @AdminOnly
    public void log() {
        System.out.println("log from ProductService\n");
    }

    @AdminOnly
    public void insert(Product product) {
        System.out.println("execute ProductService.insert(product=" + product + ")\n");
    }
}
