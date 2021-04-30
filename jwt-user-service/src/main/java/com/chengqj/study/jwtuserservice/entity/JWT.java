package com.chengqj.study.jwtuserservice.entity;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.entity
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/20 19:09
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Data
public class JWT {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private String jti;
}
