package org.example.spring_aop.transaction.service;

import org.example.spring_aop.transaction.dao.OperationLogDao;
import org.example.spring_aop.transaction.dao.UserDao;
import org.example.spring_aop.transaction.domain.OperationLog;
import org.example.spring_aop.transaction.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lifei
 */
@Component
public class DemoService {
    @Autowired
    UserDao userDao;

    @Autowired
    OperationLogDao operationLogDao;

    @Transactional
    public void addUser(String name) {
        OperationLog log = new OperationLog();
        log.setContent("create user:" + name);
        operationLogDao.save(log);

        User user = new User();
        user.setName(name);
        userDao.save(user);
    }
}
