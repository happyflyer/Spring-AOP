package org.example.spring_aop.advice;

import org.example.spring_aop.advice.service.ProductService;
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

    @Test
    public void testExecution() {
        productService.findProductNameById(1L);
        System.out.println("==========");
        try {
            productService.exceptionMethod("arg");
        } catch (IllegalAccessException ignored) {
        }
        System.out.println("==========");
        productService.addProduct(2L, "name2");
        System.out.println("==========");
        productService.deleteProduct(1L);
        System.out.println("==========");
        productService.loopUpProduct(2L);
    }
}
