package com.chengqj.study.authservice.dao;


import com.chengqj.study.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author chengqj
 * @Date 2021/1/1 10:17
 * @Desc
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
