package com.chengqj.study.jwtauthservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.jwtauthservice.config
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/17 22:41
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    // token存储策略
//        // 存于内存中
//        InMemoryTokenStore inMemoryTokenStore = new InMemoryTokenStore();
//
    // 使用jwt方式
//    @Autowired
//    JwtAccessTokenConverter jwtTokenEnhancer;
//    JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtTokenEnhancer);

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtTokenEnhancer());
    }
    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer(){
        KeyStoreKeyFactory keyStoreKeyFactory= new KeyStoreKeyFactory(new ClassPathResource("chengqj-jwt.jks"),
                "chengqj123".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("chengqj-jwt"));
        return converter;
    }

    /**
     * ClientDetailsServiceConfigurer
     * 配置客户端的一些基本信息
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() // 放在内存中
                .withClient("user-service") // 创建一个ID为service-hi的客户端
                .secret("123456") // 密码
                .authorizedGrantTypes( "refresh_token", "password") // 配置授权类型。。。
//                .authorities() // 授予客户端的权限
                .accessTokenValiditySeconds(3600) // token过期时间为3600
                .scopes("service"); // 配置域为
    }

    /**
     * AuthorizationServerEndpointsConfigurer
     * 配置
     * tokenStore: token的存储方式
     * InMemoryTokenStore 存在内存当中,如果授权服务器和资源服务器在同一个服务器当中,这无疑是一个好的选择.
     * JdbcTokenStore 存在数据库中,本例中采取的是Mysql+JPA. 这可以防止(如果存储于内存)授权服务器崩掉的时候,token全失效
     * <p>
     * authenticationManager: 只有配置了才会开启密码验证
     * userServiceDetail: 用来读取验证用户信息
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()) // 指定token存储方式
                .tokenEnhancer(jwtTokenEnhancer()) // 指定token转换器
                .authenticationManager(authenticationManager); // 开启密码验证
//                    .authorizationCodeServices() // 验证码服务
//                    .tokenGranter() // 配置Token授权者
//                    .userDetailsService(userServiceDetail); // 读取验证用户信息
    }

    /**
     * Token节点的安全策略
     * 配置获取token的策略,本例中对获取token请求不拦截,只需验证获取token的信息无误,就会返回token
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") // 获取公钥，总是允许
                .checkTokenAccess("isAuthenticated") // 验证token是否被授权
                .allowFormAuthenticationForClients() // 允许客户端来获取token
                .passwordEncoder(NoOpPasswordEncoder.getInstance()); // 验证token策略
    }

}
