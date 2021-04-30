package com.chengqj.study.jwtuserservice.dto;

import com.chengqj.study.jwtuserservice.entity.JWT;
import com.chengqj.study.jwtuserservice.entity.User;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.dto
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/20 19:11
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Data
public class UserLoginDTO {
    private JWT jwt;
    private User user;
}
