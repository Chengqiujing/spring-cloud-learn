package com.chengqj.study.jwtuserservice.service;


import com.chengqj.study.jwtuserservice.client.AuthServiceClient;
import com.chengqj.study.jwtuserservice.dao.UserDao;
import com.chengqj.study.jwtuserservice.dto.UserLoginDTO;
import com.chengqj.study.jwtuserservice.entity.JWT;
import com.chengqj.study.jwtuserservice.entity.User;
import com.chengqj.study.jwtuserservice.exception.UserLoginException;
import com.chengqj.study.jwtuserservice.util.BPwdEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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

    public User insertUser(String username,String password){
        User user= new User();
        user.setUsername(username);
        user.setPassword(BPwdEncoderUtil.BCryptPassword(password));
        return userRepository.save(user);
    }

    @Autowired
    AuthServiceClient authServiceClient;
    public UserLoginDTO login(String username,String password){
        User user = userRepository.findByUsername(username);
        if(null == user){
            throw new UserLoginException("error username");
        }
        if(!BPwdEncoderUtil.matches(password,user.getPassword())){
            throw new UserLoginException("error password");
        }

        String s = new String(Base64.getEncoder().encode("user-service:123456".getBytes(StandardCharsets.UTF_8)));
        System.out.println("base64: "+s);
        JWT jwt = authServiceClient.getToken("Basic "+s,"password",username,
                password);
        if(jwt == null){
            throw new UserLoginException("error internal");
        }
        UserLoginDTO userLoginDTO=new UserLoginDTO();
        userLoginDTO.setJwt(jwt);
        userLoginDTO.setUser(user);
        return userLoginDTO;
    }
}
