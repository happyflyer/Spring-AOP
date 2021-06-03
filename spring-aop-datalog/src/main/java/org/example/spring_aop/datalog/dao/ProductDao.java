package org.example.spring_aop.datalog.dao;

import org.example.spring_aop.datalog.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lifei
 */
public interface ProductDao extends JpaRepository<Product, Long> {
    /**
     * findById
     *
     * @param id id
     * @return Product
     */
    Product findById(Long id);
}
