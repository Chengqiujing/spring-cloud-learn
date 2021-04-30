package com.chengqj.study.jwtuserservice.exceptionhandler;

import com.chengqj.study.jwtuserservice.exception.UserLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.exceptionhandler
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/20 22:59
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@ControllerAdvice
@ResponseBody
public class MyExceptionHandler {
    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<String> handleException(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }

}
