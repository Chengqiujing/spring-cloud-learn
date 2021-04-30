package com.chengqj.study.jwtauthservice.service;


import com.chengqj.study.jwtauthservice.dao.UserDao;
import com.chengqj.study.jwtauthservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author chengqj
 * @Date 2021/1/1 10:16
 * @Desc
 */
@Service
public class UserServiceDetail implements UserDetailsService {
    @Autowired
    private UserDao userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byName = userRepository.findByUsername(username);
        return (UserDetails) byName;
    }
}
