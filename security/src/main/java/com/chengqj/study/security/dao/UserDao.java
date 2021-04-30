package com.chengqj.study.security.dao;

import com.chengqj.study.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.security.dao
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/6 16:35
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
