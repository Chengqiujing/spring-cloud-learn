package com.chengqj.study.security.service;

import com.chengqj.study.security.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.security.service
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/6 16:42
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserDao userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
