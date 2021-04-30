package com.chengqj.study.jwtuserservice.exception;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.exception
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/20 19:13
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
public class UserLoginException extends RuntimeException{
    public UserLoginException(String message) {
        super(message);
    }
}
