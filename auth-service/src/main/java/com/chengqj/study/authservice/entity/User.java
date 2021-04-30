package com.chengqj.study.authservice.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /**
         * 可以返回角色,也可以返回其它的条件
         */
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        /**
         * 这里可以返回username,也可以返回收集号码或者邮箱地址
         */
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}