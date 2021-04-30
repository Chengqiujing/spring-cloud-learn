package com.chengqj.study.jwtuserservice.client;

import com.chengqj.study.jwtuserservice.client.fallback.AuthServiceHystrix;
import com.chengqj.study.jwtuserservice.entity.JWT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.client
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/20 18:50
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@FeignClient(value="jwt-uaa-service",fallback = AuthServiceHystrix.class)
public interface AuthServiceClient {
    @PostMapping(value = "/uaa/oauth/token")
    JWT getToken(@RequestHeader(value="Authorization") String authorization, @RequestParam("grant_type") String type,
                 @RequestParam("username") String username, @RequestParam("password") String password);

}
