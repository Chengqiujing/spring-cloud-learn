package com.chengqj.study.jwtuserservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.util
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/20 15:41
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
public class BPwdEncoderUtil {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    public static String BCryptPassword(String password){
        return ENCODER.encode(password);
    }
    public static boolean matches(CharSequence rawPassword,String encodePassword){
        return ENCODER.matches(rawPassword,encodePassword);
    }
}
