package org.example.spring_aop.teach.service;

import org.example.spring_aop.teach.domain.Product;
import org.example.spring_aop.teach.security.AdminOnly;
import org.springframework.stereotype.Service;

/**
 * @author lifei
 */
@Service
public class ProductService {
    @AdminOnly
    public void insert(Product product) {
        System.out.println("insert product");
    }

    @AdminOnly
    public void delete(Long id) {
        System.out.println("delete product");
    }
}
