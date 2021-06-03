package org.example.spring_aop.teach;

import org.example.spring_aop.teach.domain.Product;
import org.example.spring_aop.teach.security.CurrentUserHolder;
import org.example.spring_aop.teach.service.ProductService;
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

    @Test(expected = RuntimeException.class)
    public void testDelete() {
        CurrentUserHolder.set("tom");
        productService.delete(1L);
    }

    @Test
    public void testInsert() {
        CurrentUserHolder.set("admin");
        productService.insert(new Product());
    }
}
