package com.chengqj.study.authservicehi.dao;

import com.chengqj.study.authservicehi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.dao
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 19:39
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
