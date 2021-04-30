package com.chengqj.study.authservicehi.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * <p>
 * 注意：这个类实现了接口UserDetails, Serializable
 * UserDetails这个类是配合Spring Security认证信息的核心接口
 * 这是一个框架接口
 * <p>
 * 其中username可以是名称,也可以是手机号,或邮件
 * 控制权限的可以是角色,也可以是用户其它信息
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description: User实体类
 * @author: chengqj
 * @date: Created in 2021/2/6 15:40
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String password;

}