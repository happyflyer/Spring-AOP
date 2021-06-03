package org.example.spring_aop.transaction.dao;

import org.example.spring_aop.transaction.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lifei
 */
public interface UserDao extends JpaRepository<User, Long> {
}
