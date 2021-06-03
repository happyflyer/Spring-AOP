package org.example.spring_aop.aspect;

import org.example.spring_aop.aspect.domain.Product;
import org.example.spring_aop.aspect.service.ProductService;
import org.example.spring_aop.aspect.service.sub.SubService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    ProductService productService;
    @Autowired
    SubService subService;

    @Test
    public void testExecution() {
        productService.getName();
        try {
            productService.exDemo();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        productService.findById(1L);
        productService.findByTwoArgs(1L, "hello");
        productService.log();
        productService.insert(new Product());
        subService.demo();
    }
}
