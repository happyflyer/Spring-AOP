package org.example.spring_aop.aspect.domain;

import org.example.spring_aop.aspect.annotation.NeedSecured;
import org.springframework.stereotype.Component;

/**
 * @author lifei
 */
@Component
@NeedSecured
public class Product {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
