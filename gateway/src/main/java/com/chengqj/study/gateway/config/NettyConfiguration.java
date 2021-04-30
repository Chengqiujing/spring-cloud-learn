package com.chengqj.study.gateway.config;

/**
 * @Author chengqj
 * @Date 2020/12/15 23:19
 * @Desc 为了解决请求时会报413的错误, 改变请求头的大小, 一般可以解决token变得太大
 */
// @Component
// public class NettyConfiguration implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {
//
//     @Value("${server.max-http-header-size:655360}") // 通过配置文件指定Header的大小
//     private int maxHeaderSize;
//
//     public void customize(NettyReactiveWebServerFactory container) {
//         System.out.println("maxHeaderSize: " + maxHeaderSize);
//         container.addServerCustomizers(builder -> builder.httpRequestDecoder(
//                 httpRequestDecoderSpec -> httpRequestDecoderSpec.maxHeaderSize(maxHeaderSize)));
//     }
//
// }
