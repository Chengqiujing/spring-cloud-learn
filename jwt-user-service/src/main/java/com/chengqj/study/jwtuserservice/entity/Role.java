package com.chengqj.study.jwtuserservice.entity;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * <p>
 * 这个类继承了GrantedAuthority,这个类也是一个框架类
 * <p>
 * getAuthority()可以返回角色名字符串,也可以是别的信息.
 * 这里的权限点是角色名
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/6 16:07
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Role() {
    }

    @Override
    public String getAuthority() {
        return name;
    }

}