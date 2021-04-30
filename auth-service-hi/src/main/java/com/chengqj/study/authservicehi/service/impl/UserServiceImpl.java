package com.chengqj.study.authservicehi.service.impl;

import com.chengqj.study.authservicehi.dao.UserDao;
import com.chengqj.study.authservicehi.entity.User;
import com.chengqj.study.authservicehi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.service.impl
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 19:44
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Service
public class UserServiceImpl implements UserService {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private UserDao userDao;

    @Override
    public User create(User user) {
        String hash = ENCODER.encode(user.getPassword());
        user.setPassword(hash);
        User u = userDao.save(user);
        return u;
    }
}
