package org.example.spring_aop.prepare.service;

import org.example.spring_aop.prepare.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lifei
 */
@Service
public class ProductService {
    @Autowired
    AuthService authService;

    public void insert(Product product) {
        authService.checkAccess();
        System.out.println("insert product");
    }

    public void delete(Long id) {
        authService.checkAccess();
        System.out.println("delete product");
    }
}
