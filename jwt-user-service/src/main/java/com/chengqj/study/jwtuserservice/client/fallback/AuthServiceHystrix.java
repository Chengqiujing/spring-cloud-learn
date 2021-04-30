package com.chengqj.study.jwtuserservice.client.fallback;

import com.chengqj.study.jwtuserservice.client.AuthServiceClient;
import com.chengqj.study.jwtuserservice.entity.JWT;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.client.fallback
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/20 19:00
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Component
public class AuthServiceHystrix implements AuthServiceClient {

    @Override
    public JWT getToken(String authorization, String type, String username, String password) {
        return null;
    }
}
