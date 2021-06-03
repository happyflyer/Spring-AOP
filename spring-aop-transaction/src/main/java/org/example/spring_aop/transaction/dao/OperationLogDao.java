package org.example.spring_aop.transaction.dao;

import org.example.spring_aop.transaction.domain.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lifei
 */
public interface OperationLogDao extends JpaRepository<OperationLog, Long> {
}
