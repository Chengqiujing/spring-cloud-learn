package com.chengqj.study.jwtauthservice.dao;


import com.chengqj.study.jwtauthservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author chengqj
 * @Date 2021/1/1 10:17
 * @Desc
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
