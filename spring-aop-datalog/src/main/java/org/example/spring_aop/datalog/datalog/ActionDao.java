package org.example.spring_aop.datalog.datalog;

import org.example.spring_aop.datalog.domain.Action;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author lifei
 */
public interface ActionDao extends MongoRepository<Action, String> {
}
