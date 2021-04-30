package com.chengqj.study.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * @Author chengqj
 * @Date 2021/1/24 13:32
 * @Desc 这个类指定了认证信息和认证规则
 * <p>
 * 这个类指定的工作
 * 1 用用的每一个请求都需要认证
 * 2 自动生成了一个登录表单
 * 3 可以用username和password来进行认证
 * 4 用户可以注销
 * 5 阻止了CSRF攻击
 * 6 Session Fixation 保护
 * 7 安全Header集成了以下内容
 * ...
 * 8 集成了以下的servlet API方法
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;

    /**
     * 认证
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance()); // 配置从数据库获取

        auth.inMemoryAuthentication() // 内存存储
                .passwordEncoder(new BCryptPasswordEncoder()) // 指定密码加密器
                .withUser("forezp") // 指定用户名
                .password(new BCryptPasswordEncoder().encode("123456")) // 指定用户密码并加密
                .roles("USER"); // 指定用户角色

        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("ADMIN", "USER");
    }

    /**
     * 配置资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/index").permitAll() // 这两个路径下的资源不需要验证
                .antMatchers("/user/**").hasRole("USER") // /user路径下需要角色USER
                .antMatchers("/blogs/**").hasRole("USER") // /blogs路径下需要角色USER
                .and()
                .formLogin().loginPage("/login") // 表单登录地址是/login
                .failureUrl("/login-error") // 登录失败的地址是/login-error
                .and()
                .exceptionHandling().accessDeniedPage("/401"); // 异常处理重定向到/401
        http.logout().logoutSuccessUrl("/");
    }

}
