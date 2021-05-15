# Spring Cloud

Spring Cloud学习笔记



## 架构演变



### 单体架构的不足

- 业务复杂导致代码量大
- 单体并发有限
- 部分异常影响整体
- 持续交付能力差

> 单体架构集群并没有解决单体存在的问题



### 微服务达到的目的

- 可以按照一定维度划分为一个独立运行的服务单元
- 服务之间轻量级通信 (如HTTP)
- 自动化部署
- 可以使用不同的语言
- 可以使用不同的存储技术
- 服务集中化管理
- 微服务本身是一个分布式系统, 天生带有分布式的优势



### 微服务的缺点

- 分布式有的缺点微服务都有(例如: 分区, 分布式事务)

- 微服务部署

- 微服务划分

- 微服务的复杂度

  服务一旦多个必然会带来服务治理的问题, 例如: 服务发现, 服务追踪, 服务治理

> 要不要使用微服务一定要客观判断, 某种程度上来讲, 微服务和单体并没有优劣之分, 只是在不同业务量级, 不同体系中的不同选择而已



## 微服务的实现原则



### 微服务单元拆分

- 满足AKF拆分立方

  横向(x轴): 水平复制

  纵向(y轴): 按一定维度拆分为多个服务(属于一个系统)

  垂直(z轴): 数据分区, 也就是分为多个系统(数据分片,分区),系统相互隔离,但完整

- 这里所讲的拆分一般就是指纵向拆分

  一般会就业务拆分



### 轻量级通信

- 通信方式与平台,语言无关

  ~~普遍通用的通信方式~~

- 通信协议轻量级

  如HTTP通讯协议,REST风格

- 数据格式轻量级
  1. 一种是 JSON或者XML, JSON更轻量
  2. 另一种是用Protobuf进行序列化为二进制(需要反序列化,可读性差)

> 尽管HTTP+JSON是一种不错的选择,但这种通信不是可靠通信, 虽然成功率高,但有失败的时候



### 数据库独立

- 每个微服务有属于自己的数据库, 这样做扩展复制不会受影响
- 数据库可以不同类型,根据存储需要选择



### 服务自动部署

- DevOps --> jenkins



### 服务集中化管理

须有注册中心



### 分布式架构

- CAP理论下, 分布式P是基本要求

- 分布式需实现服务的独立性和服务相互调用的可靠性
  1. 分布式事务
  2. 全局锁(分布式锁)
  3. 全局唯一ID
  4. 共享数据



### 熔断机制

- 防止雪崩
- 高可用的依靠
- 熔断组件
  1. 服务熔断
  2. 服务重启(自我修复)
  3. 服务监听



### 服务监控(追踪)

服务众多,要有监控,报警策略



### 服务无状态

一旦涉及到服务水平复制, 就应考虑服务的无状态化, 也就是将一个存在于服务本身,却被其他服务依赖的数据( 缓存 )共享化, 让服务不在单独持有, 不然会带来数据的不一致性( 不同步 )



## 实现方式

- 以Spring Cloud为解决策略的微服务实现方式

- 以K8S在容器的基础上实现方式



## 完整微服务需要的模块

针对微服务的实现模式,应具有的功能

- 按照业务划分服务,代码量小,业务单一,易于维护
- 每个微服务都有自己独立的基础组件, 如数据库,缓存等
- 服务之间通信机制
- 一套完整的服务治理方案
- 单个微服务能够集群部署(无状态,负载均衡)
- 微服务系统应有安全机制( 权限验证, 用户验证, 资源保护)
- 服务链路追踪能力
- 一套完整的日志系统



### 模块功能实现

- 服务的注册和发现

  注册

  下线

  发现

  故障

- 服务的负载均衡

  方式: 

  1. 客户端: ribbon
  2. 服务端: nginx

  策略

  1. 轮询

  2. 随机

  3. 最少请求

- 服务的调用

  协议: http/tcp

  请求方式:

  1. RestTemplate
  2. HttpClient 
  3. fegin

- 服务的容错

  反馈

  降级

  限流

  监控( 时间窗口 )

  重唤醒

- 服务的网关

  路由:  定位到服务

  过滤器: 所有请求都经过,适合做鉴权

- 服务统一配置管理

- 服务链路追踪

- 服务实时日志



### 各部分实现架构原理

#### 服务的注册和发现

服务的注册,下线, 发现, 故障



### 各部分具体实现

#### eureka注册中心

服务发现与治理

**properties**

```properties
# eureka默认端口 8761
server.port=8761
spring.application.name=eureka

# 以ip方式注入到eureka,否则指定到eureka的是hostname
eureka.instance.prefer-ip-address=true
eureka.instance.hostname=localhost
# eureka服务不自注册
eureka.client.register-with-eureka=false
# 不同步服务
eureka.client.fetch-registry=false
# eureka默认访问地址
eureka.client.service-url.default-zone=http://${eureka.instance.hostname}:${server.port}/eureka

```

**pom**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>eureka</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>eureka</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>

```

**启动类注解**

```java
@SpringBootApplication
@EnableEurekaServer // 启动eureka
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}
```



#### eureka客户端

**properties**

```properties
server.port=8762
spring.application.name=eureka-client

eureka.client.service-url.default-zone=http://localhost:8761/eureka/
```

**pom**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>eureka-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>eureka-client</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>

```

**启动类配置**

```java
@SpringBootApplication
@EnableEurekaClient
public class EurekaClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
```



#### ribbon负载均衡

ribbon有众多组件,常用的有

- ribbon-loadbalancer  独立使用
- ribbon-eureka  结合eureka客户端
- ribbon-core   核心api

**开启eureka**

```java
@SpringBootApplication
@EnableEurekaClient
public class RibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }
}
```

**properties**

```properties
server.port=8082
spring.application.name=ribbon-test
eureka.client.service-url.default-zone=http://localhost:8761/eureka/
```

**pom**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>ribbon</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>ribbon</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>

```

**java代码**

##### 第一种方式 :  @LoadBalanced

config

```java
/**
 * @Author chengqj
 * @Date 2020/12/1 18:11
 * @Desc
 */
@Configuration
public class RibbonConfig {
    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}

```

controller

```java
@RestController
public class RibbonController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/ribbon")
    public String test(){
        String url = "http://eureka-client/hello";  // 这里hostname指定为eureka的应用名
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

}
```

##### 第二种方式: LoadBalancerClient

controller

```java
@RestController
public class RibbonController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @GetMapping("ribbon2")
    public String test2(){
        ServiceInstance choose = loadBalancerClient.choose("eureka-client");
        String url = "http://"+choose.getHost()+":"+choose.getPort()+"/hello";
        System.out.println(url);
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }
    
}
```

这种方式单机测试是没有问题的, 可是ServiceInstance中getHost()获取的是主机名,这样的话,当非单机测试时,又无DNS,可能会出现找不到服务的情况.

对于这种情况,我觉得可以将ServiceInstance转为具体类型, 然后获取ip地址来请求.

##### 自定义服务列表

如若我们禁用ribbon-eureka从eureka中获取服务列表,且自定义服务列表,配置如下

```properties
# 关闭获取服务列表
ribbon.eureka.enable=false
# 自定义服务列表
stores.ribbon.listOfServers=baidu.com,qq.com
```

java

```java
@RestController
public class RibbonController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    /**
     * 自定义服务列表
     */
    @GetMapping("/ribbon3")
    public String test3(){
        ServiceInstance choose = loadBalancerClient.choose("stores");
        String result = restTemplate.getForObject(choose.getUri(), String.class);
        return result;
    }
}

```

这里特殊需要注意的是, `stores.ribbon.listOfServers`中最后的`listOfServers`必须保持原样,而不能写做list-of-servers



#### Feign调用服务

声明式远程API调用

**启动配置**

```java
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class FeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignApplication.class, args);
    }

}

```

**pom**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>feign</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>feign</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>

```

**java代码**

**feign的config类**

可以配置很多feign默认的配置项,具体可以从`FeignClientsConfiguration`中看默认配置,这个类在spring-cloud-openfeign-core下

```java
@Configuration
public class FeignConfig {
    @Bean
    public Retryer feignRetryer(){
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 5);
    }
}
```

feign的API声明

@Feign中属性的介绍

- value 指定eureka服务名,同name
- configuration  feign的调用项,如
- name 同value
- qualifier 作用于注解@Qualifier
- url  可以填写要调用服务的url, 这就相当于直接调用
- fallback  熔断时的逻辑
- primary  默认为true,当有fallback的时候起作用, 因为fallback的实现是继承于Feign声明的接口,属于同一类型,用primary属性标识

```java
@FeignClient(value = "eureka-client",configuration = FeignConfig.class)
public interface HelloCall {
    @GetMapping("/hello")
    String sayHello(@RequestParam("name") String name);

}
```

feign的调用

```java
@RestController
public class FeignController {
    @Autowired
    HelloCall helloCall;

    @GetMapping("/feign")
    public String get(){
        return helloCall.sayHello("这是Feign内容");
    }
}
```

**如果想在feign中使用HttpClient或者OkHttp的话**

Feign是通过HttpURLConnection来实现网络请求的.

只需引入相关依赖就好

```xml
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-httpclient</artifactId>
    <version>RELEASE</version>
</dependency>
```

至于OkHttp也是一样的

```xml
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-okhttp</artifactId>
    <version>RELEASE</version>
</dependency>
```



#### 熔断器Hystrix

- Netflix开源

- 提供熔断功能,防止出现联动故障
- 提供回退(falback)方案
- 有可视化面板

> 受限要明白,熔断是客户端的行为, 因为当服务不可调用时,服务端多半时宕机状态, 所以策略不能在服务端执行

##### 在RestTempalte和Ribbon上使用熔断器

pom

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

启动配置

```java
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class RibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }

}
```

Demo代码

熔断可以在调用方法上加`@HystrixCommand(fallbackMethod = "hiError")`其中hiError就是熔断逻辑

```java
@Service
public class RibbonService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    public String hi(String name){
        return restTemplate.getForObject("http://eureka-client/hello", String.class);
    }
    // 熔断逻辑
    public String hiError(String name){
        return "hi," + name + ",sorry,error!";
    }
}
```



##### 在Feign上使用Hystrix

由于Feign默认依赖中有Hystrix所以我们只需要开启就好

```properties
server.port=8083
spring.application.name=feign-client
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
# 开启自带的hystrix
feign.hystrix.enabled=true
```

fallback逻辑

```java
@Component
public class HystrixFallback implements HelloCall {
    @Override
    public String sayHello(String name) {
        return "Hystrix fallback: ~~~";
    }
}
```

添加fallback

```java
@FeignClient(value = "eureka-client", configuration = FeignConfig.class,fallback = HystrixFallback.class)
public interface HelloCall {
    @GetMapping("/hello")
    String sayHello(@RequestParam("name") String name);

}
```

##### Hystrix Dashboard

###### 可视化服务配置

pom

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>
```

配置**(注意:当使用的spring boot版本为2.x以上的时候,必须配置)**

```properties
# 不添加前端页面会报错EventSource's response has a MIME type ("text/plain") that is not "text/event-stream". Aborting the connection.
hystrix.dashboard.proxy-stream-allow-list=localhost
```

启动类

```java
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }

}
```

###### **要监视的服务端配置**

pom

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

properties

```properties
# 开启actuator的部分健康内容,或者*全开
management.endpoints.web.exposure.include=hystrix.stream,info,health
```

启动类

```java
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class RibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }

}
```

配置完成后可请求localhost:port/hystrix , 在面板中输入单机访问地址 http://localhost:port/actuator/hystrix.stream, 指定访问时间频率, 和app名字, 之后变可完成监控

> **注意**: 在Spring boot 2.0以上的时候,Dashboard `必须在配置文件配置hystrix.dashboard.proxy-stream-allow-list=localhost`,否则前端会报错
>
> ### Hystrix报错：Unable to connect to Command Metric Stream
>
> 并且前端控制台会报
>
> ### `EventSource's response has a MIME type ("text/plain") that is not "text/event-stream". Aborting the connection.`

>对于上述问题
>
>网上有说和Servlet有关的,还需要做配置. 配置类在源码中也有--HystrixDashboardConfig 
>
>但是,我并不是通过配置类解决的. 虽然描述的问题一样



###### 集群下Hystrix的监控(Turbine聚合监控)

Turbin的监控是建立在Hystrix的返回数据的基础上,所以保证要监控的项目必须具备上述的服务端配置

Turbin的监控对Feign和@@HystrixCommand都起作用

**Turbine搭建**

pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.11.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>hystrix-dashboard</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>hystrix-dashboard</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.SR9</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-turbine</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```

启动类

```java
@SpringBootApplication
@EnableEurekaClient
@EnableHystrixDashboard
@EnableTurbine
public class HystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }

}
```

properties配置

```properties
server.port=8087
spring.application.name=dashboard
eureka.client.service-url.default-zone=http://localhost:8761/eureka/

# 不添加前端页面会报错EventSource's response has a MIME type ("text/plain") that is not "text/event-stream". Aborting the connection.
hystrix.dashboard.proxy-stream-allow-list=localhost

#
turbine.combine-host-port=true
# 配置要监控的服务名,eureka显示的名字
turbine.app-config=ribbon-test,feign-client
# 默认为服务名的集群,用默认
turbine.cluster-name-expression=new String("default")
# 可以不写,默认就是default
turbine.aggregator.cluster-config=default
# 指定监控Url,不指定的话默认为/actuator/hystrix.stream
turbine.instanceUrlSuffix=/actuator/hystrix.stream
```

之后便进入 http://localhost:8087/hystrix 进入面板, 在面板中,按照集群(cluster)的方式填写地址,如: http://localhost:8083/turbine.stream 指定delay和名字,就可以看到了



#### 路由网关 Zuul

可以实现API接口聚合,统一暴露

可以做身份认证和权限认证

可以监控,日志

可以做降级

##### 原理

路由+过滤器

##### 过滤器

过滤器分四种

- PRE 请求路由到服务之前

- ROUTING  将请求路由到具体的服务,它使用Http Client进行请求
- POST  请求路由到具体服务
- ERROR  其他过滤器发生错误时执行

过滤器对象有四个属性:

- Type 过滤器类型
- Execution Order 执行顺序,越小越优先
- Criteria 过滤器执行所需条件
- Action 过滤器的逻辑

##### 搭建路由服务

路由由配置文件中的配置决定,具体看配置文件中解释

pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.cehngqj.study</groupId>
    <artifactId>zuul</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>zuul</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>

```

启动类

```java
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }

}
```

properties

```properties
server.port=8084
spring.application.name=zuul
eureka.client.service-url.default-zone=http://localhost:8761/eureka/

# path 指定路由url
zuul.routes.hiapi.path=/hiapi/**
# 指定路由到的服务
zuul.routes.hiapi.service-id=eureka-client

zuul.routes.ribbonapi.path=/ribbonapi/**
zuul.routes.ribbonapi.service-id=ribbon-test

zuul.routes.feignapi.path=/feignapi/**
zuul.routes.feignapi.service-id=feign-client
# 指定url后就停止负载均衡
#zuul.routes.feignapi.url=http://localhost:8082

# 可以添加版本号作为前缀
zuul.prefix=/v1
```

每个路由都由 path + serviceId 组成, path时路由的url, serviceId是路由的服务

配置中指定了版本号作为前缀

配置中如果为每个路由指定url则会失去负载均衡的能力

指定url后, 如果想继续保持负载均衡,则需按下列配置

```properties
# 如果指定url还想做负载均衡,则用以下形式
zuul.routes.noribbon.path=/noribbon/**
zuul.routes.noribbon.service-id=haiapi-v1
haiapi-v1.ribbon.listOfServers=http://localhost:port,http://localhost:port
```

##### Zuul的熔断

pom如上.



实现`FallbackProvider`接口中的方法

```java
@Component
public class MyZuulFallbackProvider implements FallbackProvider {

    // 指定用于那个路由熔断,如果所有路由都需熔断则用*
    @Override
    public String getRoute() {
        return "eureka-client";
        // return "*"; // 对所有路由熔断
    }

    // 熔断逻辑
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {

        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("ooooops!error,i'm the fallback".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
```



##### 搭建过滤器

不需要其他引入, pom如上

Zuul的过滤器通过继承`ZuulFilter`,实现其中的抽象方法filterType() 和 filterOrder(), 以及IZuulFilter的shouldFilter() 和Object run()的两个方法 . 对应的解释看代码中的注释.



过滤器分为四种

```java
private static final String PRE_TYPE = "pre"; // 路由前
private static final String POST_TYPE = "post"; // 路由后
private static final String ROUTING_TYPE = "routing"; // 路由中
private static final String ERROR_TYPE = "error"; // 其他过滤器报错
```



过滤器的java实现方式

```java
@Component
public class MyZuulFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(MyZuulFilter.class);

    private static final String PRE_TYPE = "pre"; // 路由前
    private static final String POST_TYPE = "post"; // 路由后
    private static final String ROUTING_TYPE = "routing"; // 路由中
    private static final String ERROR_TYPE = "error"; // 其他过滤器报错
    @Override
    public String filterType() { // 指定过滤器是那种类型
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() { // 指定过滤器的优先级, 数字越小越优先
        return 0;
    }

    @Override
    public boolean shouldFilter() { // 指定过滤器是否生效
        return true;
    }

    @Override
    public Object run() throws ZuulException { // 校验逻辑
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getParameter("token");
        if (token == null) {
            log.warn("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);

            try {
                ctx.getResponse().getWriter().write("token is empty");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        log.info("ok");
        return null;
    }
}
```

Zuul是采用了类似SpringMVC的DispatchServlet来实现的,采用的是异步阻塞模型,所以性能比Nginx要差.

Zuul的常用使用方式

- 一种是对不同渠道使用不同的Zuul来进行路由
- 另一种是通过Nginx和Zuul相互结合来实现负载均衡



#### 服务网关 - Spring Cloud Gateway

Zuul和Gateway的区别

- Gateway使用非阻塞方式
- Zuul基于servlet,采用HttpClient进行请求转发,使用阻塞模式

两者几乎可以无缝切换

因为Gateway也有 route 和 过滤器, 不一样的是多了一个断言(Predicate), 用以作为路由的判断.

Gateway内置了一部分断言, 由对应的断言工厂提供

**pom**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>gateway</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>gateway</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
<!--        <dependency>-->
<!--            <groupId>org.springframework.cloud</groupId>-->
<!--            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>-->
<!--        </dependency>-->

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>

```

> 注意：切记 gateway不可引，不然会报错
>
> ```xml
> <dependency>
>     <groupId>org.springframework.boot</groupId>
>     <artifactId>spring-boot-starter-web</artifactId>
> </dependency>
> ```



无启动类配置



##### Gateway路由

路由断言由断言工厂来决定

内置的断言工厂有:

| 类别                  | 作用对象   | 工厂                            | 描述                         |
| --------------------- | ---------- | ------------------------------- | ---------------------------- |
| 路                    | DateTime   | AfterRoutePredicateFactory      | 请求时间在配置的时间之后     |
| 由                    | DateTime   | BeforeRoutePredicateFactory     | 请求时间在配置的时间之前     |
| 工                    | DateTime   | BetweenRoutePredicateFactory    | 请求时间在配置的时间之间     |
| 厂                    | Cookie     | CookieRoutePredicateFactory     | 请求携带的Cookie满足配置的职 |
| RoutePredicateFactory | Header     | HeaderRoutePredicateFactory     | 请求携带Header满足配置的值   |
| 路                    | Host       | HostRoutePredicateFactory       | 请求的Host满足配置的值       |
| 由                    | Method     | MethodRoutePredicateFactory     | 请求的Method满足配置的值     |
| 工                    | Path       | PathRoutePredicateFactory       | 请求路径满足配置的值         |
| 厂                    | QueryParam | QueryRoutePredicateFactory      | 请求参数满足配置的值         |
|                       | RemoteAddr | RemoteAddrRoutePredicateFactory | 请求地址满足配置的值         |

###### **DateTime断言工厂**

相对于一个确定的时间点DateTime, 有Before, After, Between有三种断言

使用配置

```properties
spring:
  profiles:
    active: aftert_oute
---
server:
  port: 8085
spring:
  profiles: aftert_oute
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
      routes:
        # 路由ID
      - id: after_route
          # 路由到uri
        uri: http://www.baidu.com
          # After时间断言
        predicates:
        - After=2021-01-20T17:42:47.789+08:00[Asia/Shanghai]
```

>  ~~需要注意~~
>
>  ~~--- 用来分套配置的语法只在yml文件中起作用, 我在properties中并未得到效果~~
>
> ~~还有就是~~
>
> ~~gateway:~~
>       ~~discovery:~~
>         ~~locator:~~
>           ~~enabled: true~~
>
> ~~这项配置,在我使用的Gateway版本中必须有,否则不起起到作用~~



###### **Header断言工厂**

Header中有相应key和value

```properties
spring:
  profiles:
    active: header_route
---
# Header断言工厂
spring:
  profiles: header_route
  cloud:
    gateway:
      routes:
        # 路由ID
        - id: header_route
          # 路由到uri
          uri: http://www.baidu.com
          # 断言模式
          predicates:
            - Header=X-Request-Id, \d+
```



###### **Cookie断言工厂**

Cookie当中有key和value就会转发

```properties
spring:
  profiles:
    active: cookie_route
---
# Cookie断言工厂
spring:
  profiles: cookie_route
  cloud:
    gateway:
      routes:
        # 路由ID
        - id: cookie_route
          # 路由到uri
          uri: http://www.baidu.com
          # 断言模式
          predicates:
            - Cookie=name, asdf
```



###### **Host断言工厂**

Host当中有相应的key,value

```properties
spring:
  profiles:
    active: host_route
---
# Host断言工厂
spring:
  profiles: host_route
  cloud:
    gateway:
      routes:
        # 路由ID
        - id: host_route
          # 路由到uri
          uri: http://www.baidu.com
          # 断言模式
          predicates:
            - Host=**.btbullet.com
```



###### **Method断言工厂**

对应GET,POST,PUT,DELETE进行路由

```properties
spring:
  profiles:
    active: method_routew
---
# Method断言工厂
spring:
  profiles: method_routew
  cloud:
    gateway:
      routes:
        # 路由ID
        - id: method_routew
          # 路由到uri
          uri: http://www.baidu.com
          # 断言模式
          predicates:
            - Method=GET
```



###### **Path断言工厂**

整对Path进行转发,可以是spel表达式

```properties
spring:
  profiles:
    active: path_route
---
# Path断言工厂
spring:
  profiles: path_route
  cloud:
    gateway:
      routes:
        - id: path_route
          uri: http://www.baidu.com
          predicates:
            - Path=/
```

> 这里要注意的是: - Path=/go 如果对应的 uri: http://www.baidu.com 这样路由过去后是  http://www.baidu.com/go  ,所以如果没有对应的资源会报404



###### **Query断言工厂**

请求参数可以一个,也可以多个, 可以有值, 也可以没值

```properties
spring:
  profiles:
    active: query_route
---
# Query断言工厂,请求参数断言工厂
spring:
  profiles: query_route
  cloud:
    gateway:
      routes:
        - id: query_route
          uri: http://www.baidu.com
          predicates:
            - Query=foo, ba
            - Query=ff, bb
            - Query=aa
```



##### Gateway过滤器

- 过滤器根据作用范围有全局过滤器和单路由过滤器, 这点Zuul是没有的

- 过滤器也有pre和post之分
- 可以作用在特定请求路径上
- 同断言工厂相似, 包含多个内置网关过滤工厂

**服务网关内置的过滤器工厂**

这些网关过滤器工厂的使用, 跟路由的使用相似, 以约定的配置方式, 进行配置, 就可以快捷使用

| 过滤器名称                                 | 描述                   |
| ------------------------------------------ | ---------------------- |
| AddRequestHeader GatewayFilter Factory     | 添加请求头网关过滤工厂 |
| AddRequestParameter GatewayFilter Factory  | 添加请求参数...        |
| AddResponseHeader GatewayFilter Factory    | 添加响应头...          |
| Hystrix GatewayFilter Factory              | Hystrix熔断的...       |
| PrefixPath GatewayFilter Factory           | PrefixPath的...        |
| PreserveHostHeader GatewayFilter Factory   | 保留原请求头...        |
| RequestRateLimiter GatewayFilter Factory   | 请求限流的...          |
| RedirectTo GatewayFilter Factory           | 重定向的网关....       |
| RemoveRequestHeader GatewayFilter Factory  | 删除请求头的...        |
| RemoveResponseHeader GatewayFilter Factory | 删除相应头的...        |
| RewritePath GatewayFilter Factory          | 重写路径的...          |
| SaveSession GatewayFilter Factory          | 保存会话的...          |
| SecureHeaders GatewayFilter Factory        | 安全头的...            |
| SetPath GatewayFilter Factory              | 设置路径的...          |
| SetResponseHeader GatewayFilter Factory    | 设置响应的...          |
| SetStatus GatewayFilter Factory            | 设置状态的...          |
| StripPrefix GatewayFilter Factory          | StripPrefix的...       |
| Retry GatewayFilter Factory                | 重试的...              |



###### 添加请求头示例

内置过滤器工厂: AddRequestHeader GatewayFilter Factory

配置

```properties
spring:
  cloud:
    gateway:
      routes:
        - id: add_header
          uri: http://localhost:8080
          predicates:
            - After=2020-01-20T17:42:47.789+08:00[Asia/Shanghai]
          filters:
            - AddRequestHeader=aaa, bbb
```

java验证代码

```java
@GetMapping("/hello")
    public String sayHello(String name, HttpServletRequest request) {
        String header = request.getHeader(name);
        System.out.println("header: "+header);
        return "Hello ~ port:8080";
    }
```

> 这里必须有一点要注意, 我在此遇到了404的报错, 并且寻找错误很久.
>
> 还原:
>
> 请求: http://localhost:8085
>
> 配置: 如上, 指向uri: http://localhost:8080/hello
>
> 如此会报404.
>
> 要注意的是, gateway并不是将请求uri进行替换, 而是将请求的服务源路由另一个
>
> 所以正确的写法
>
> 请求: http://localhost:8085/hello
>
> 配置: 如上, uri: http://localhost:8080
>
> 还有一点需要注意, 这是请求头添加参数, 所以要在请求到达验证, 同理, 还有返回头添加参数
> 还有需要注意的是, 不要在gateway服务上写请求验证, 应为路由到自己服务, 可能会引起死循环, 如请求符合要求,又路由到自己,又进行验证,如此循环





###### 重写路径

内置过滤器工厂: RewritePath GatewayFilter Factory

```properties
spring:
  cloud:
    gateway:
      routes:
        - id: rewrite_path
          uri: http://localhost:8080
          predicates:
            - Path=/foo/**
          filters:
            - RewritePath=/foo/(?<segment>.*), /$\{segment}
```

> 路径的规则还不是很清楚，segment就不是很明白。还需进一步学习。



##### 自定义过滤器

1. 自定义过滤器实现类， 需要继承`GatewayFilter`，`Ordered`。
2. 通过配置类, 注册过滤器

实现两个接口中的

- ```
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
  ```

- ```
  public int getOrder()
  ```

###### **实现类**

```java
public class RequestTimeFilter implements GatewayFilter, Ordered {
    private static final Log log = LogFactory.getLog(GatewayFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 相当于pre方法 -- 开始
        exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
        // pre结束

        return chain.filter(exchange).then(Mono.fromRunnable(()->{ // 相当于post方法 -- 开始
            Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
            if (startTime != null) {
                log.info(exchange.getRequest().getURI().getRawPath()+": "+(System.currentTimeMillis()-startTime)+"ms");
            }
        }));
        // 结束
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
```

结构解析

```java
public class RequestTimeFilter implements GatewayFilter, Ordered {
    private static final Log log = LogFactory.getLog(GatewayFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        
        // pre方法逻辑

        return chain.filter(exchange).then(
            Mono.fromRunnable(()->{
                
            // post方法逻辑
                
        	})
        );
        // 结束
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
```



###### **注册过滤器**

```java
/**
 * @Author chengqj
 * @Date 2020/12/17 22:14
 * @Desc
 *  注册自定义过滤器到路由
 */
@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder){
        return builder.routes().route(r ->
                r.path("/hello/**") // 指定那种路由
                .filters(f ->
                        f.filter(new RequestTimeFilter()) // 自定义过滤器
                        .addRequestHeader("X-Response-Default-Foo","Default-Bar") // 添加返回头过滤器
                )
                .uri("http://localhost:8080") // 要路由的uri
                .order(0) // 优先级,越小越优先
                .id("customer_filter_router")
        ).build();
    }


}
```



##### 自定义过滤器工厂

过滤器和过滤器工厂作用是一样的, 但过滤器工厂可以像内置工厂一样通过配置文件使用.

步骤

1. `extends AbstractGatewayFilterFactory<RequestTimeGatewayFilterFactory.Config>`

   其中`RequestTimeGatewayFilterFactory.Config`是指 `实现类.内部类`, 这个内部类的作用是重写public GatewayFilter apply(Config config)方法时作为接收Boolean类型参数的类参数, 该参数用于决定是否打印参数, 类的变量名可以随意写.

   重写

   - public List<String> shortcutFieldOrder()
   - public GatewayFilter apply(Config config) 

2. 注册工厂

3. 配置文件中使用

###### **实现类**

```java
/**
 * @Author chengqj
 * @Date 2020/12/19 14:38
 * @Desc 自定义过滤器工厂, 还需注册工厂才能使用
 */
public class RequestTimeGatewayFilterFactory  extends AbstractGatewayFilterFactory<RequestTimeGatewayFilterFactory.Config> {
    private static final Log log = LogFactory.getLog(GatewayFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";
    private static final String KEY = "withParams";

    public RequestTimeGatewayFilterFactory() {
        super(Config.class);  // 这个super必须写
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY); // 指定参数列表? 不知道什么作用,暂时标记
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain)->{
            exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                        if (startTime != null) {
                            StringBuilder bs = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                                    .append(": ")
                                    .append(System.currentTimeMillis() - startTime)
                                    .append("ms--原则过滤器工厂");
                            if (config.isWithParams()) {
                                bs.append(" params:")
                                  .append(exchange.getRequest().getQueryParams());
                            }
                            log.info(bs.toString());
                        }
                    })
            );
        };
    }

    /**
     * 这个内部类是用于重写public GatewayFilter apply(Config config)时,接收参数用, 这和泛型中指定的类型有关系
     */
    public static class Config{
        private boolean withParams;
        public boolean isWithParams(){
            return withParams;
        }
        public void setWithParams(boolean withParams){
            this.withParams = withParams;
        }
    }
}
```

结构解析

```java
public class RequestTimeGatewayFilterFactory  extends AbstractGatewayFilterFactory<RequestTimeGatewayFilterFactory.Config> {
    
    private static final String KEY = "withParams";

    public RequestTimeGatewayFilterFactory() {
        super(Config.class);  // 这个super必须写
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY); // 指定参数列表? 不知道什么作用,暂时标记
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain)->{
            
            // pre方法逻辑
            
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        
                        // post方法逻辑
                        
                    })
            );
        };
    }
}
```



###### **注册工厂**

```java
/**
 * @Author chengqj
 * @Date 2020/12/19 15:11
 * @Desc 将过滤器工厂配置注册
 */
@Configuration
public class FilterFactoryConfig {
    @Bean
    public RequestTimeGatewayFilterFactory elapseGatewayFilterFactory(){
        return new RequestTimeGatewayFilterFactory();
    }
}
```

###### **文件配置**

```properties
spring:
  cloud:
    gateway:
      routes:
        - id: add_header
          uri: http://localhost:8080
          predicates:
            - After=2020-01-20T17:42:47.789+08:00[Asia/Shanghai]
          filters:
            - RequestTime=true
```



##### 全局过滤器

Gateway根据作用范围分为网关过滤器(GatewayFilter)和全局过滤器(GlobalFilter)

- GatewayFilter: 通过spring.cloud.toutes.filters配置在具体的路由下,之作用在当前路由上;或者通过spring.cloud.default-filters配置在全局中,作用在所有路由上
- GlobalFilter: 不需要在配置文件中配置, 作用在所有路由上. 最终通过GatewayFilterAdapter包装成GatewayFilterChain可识别的过滤器. 它是将请求业务以及路由的Url装换成真实业务服务的请求地址的核心过滤器, 不需要配置, 系统初始化的时候加载, 并作用在每个路由上

全局过滤器同样有很多内置过滤器使用



| 内置全局过滤器类  |             类名             |         描述         |
| :---------------: | :--------------------------: | :------------------: |
|   LoadBalancer    |   LoadBalancerClientFilter   |    负载均衡过滤器    |
|    HttpClient     |       NettyRouteFilter       | Http客户端相关过滤器 |
|    HttpClient     |  NettyWriterResponseFilter   | Http客户端相关过滤器 |
|     WebSocket     |    WebSocketRoutingFilter    | WebSocket相关过滤器  |
|    ForwardPath    |      ForwardPathFilter       |    路径转发过滤器    |
| RouteToRequestUrl |   RouteToRequestUrlFilter    |  转发路由Url过滤器   |
|     WebClient     |  WebClientHttpRoutingFilter  | WebClient相关过滤器  |
|     WebClient     | WebClientWriteResponseFilter | WebClient相关过滤器  |

全局过滤器不需要配置文件配置

直接使用配置类配置即可, 其使用和自定义全局的使用是一样的, 所以在这里只介绍自定义的用法

###### 自定义全局过滤器

- 实现 implements GlobalFilter, Ordered中的方法

- 配置类注册

实现类

```java
/**
 * @Author chengqj
 * @Date 2020/12/20 0:17
 * @Desc 自定义全局过滤器, 全局处理器不需要配置,作用于所有
 *  实现过滤有token的就转发
 */
public class SelfTokenGloableFilter implements GlobalFilter, Ordered {
    Logger logger = LoggerFactory.getLogger(SelfTokenGloableFilter.class);  // 日志类

    // 全局过滤器要实现的方法
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // pre方法
        String token = exchange.getRequest().getQueryParams().getFirst("token"); // 获取token
        if (Objects.isNull(token)|| token.isEmpty()) { // 如果tocken为空,则拒绝请求
            logger.info("token is empty");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange); // 如果token不为空,则继续
    }

    @Override
    public int getOrder() { // 请求优先级, 越小越优先
        return -100;
    }
}
```

注册全局过滤器

```java
@Configuration
public class GloableFilterConfig {
    @Bean
    public SelfTokenGloableFilter tokenGloableFilter(){
        return new SelfTokenGloableFilter();
    }
}
```



##### 限流

限流的作用

- 防止流量突发
- 防止流量攻击

常见限流方式

- Hystrix使用线程池隔离
- 时间窗口
- 活动窗口
- 令牌桶
- 漏桶

限流维度

- IP限流(用户限流)
- Url限流(资源限流)
- 用户访问次数

限流发生位置

- 网关层 如Nginx,Zuul,Gateway,Openresty, Kong
- 应用层, 通过AOP去做限流

常见的限流算法

- 计数器算法 (固定窗口)
- 漏桶算法
- 令牌桶算法

###### 网关限流

在pre类型的过滤器中实现上述3种限流算法.

Gateway的限流实现,官方提供了RequestRateLimiterGatewayFilterFactory这个类,使用Redis和Lua脚本实现令牌同算法进行限流.

**pom文件**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>gateway</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>gateway</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <!--        <dependency>-->
        <!--            <groupId>org.springframework.boot</groupId>-->
        <!--            <artifactId>spring-boot-starter-web</artifactId>-->
        <!--        </dependency>-->
<!--        利用redis做限流 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <!--        <dependency>-->
        <!--            <groupId>org.springframework.cloud</groupId>-->
        <!--            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>-->
        <!--        </dependency>-->

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>
```

**限流解析器实现类**

```java
/**
 * @Author chengqj
 * @Date 2020/12/20 17:28
 * @Desc 根据HostName进行限流
 */
public class HostAddrKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        System.out.println("执行Hostname限流");
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}

/**
 * @Author chengqj
 * @Date 2020/12/20 21:44
 * @Desc 通过用户限流
 */
public class UserKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }
}

/**
 * @Author chengqj
 * @Date 2020/12/20 21:44
 * @Desc 通过Url限流
 */
public class UrlKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getURI().getPath());
    }
}

```

**注册解析器**

```java
/**
 * @Author chengqj
 * @Date 2020/12/20 17:36
 * @Desc 将限流类进行注册
 */
@Configuration
public class XianliuConfig {
    @Bean
    public HostAddrKeyResolver hostAddrKeyResolver() {
        return  new HostAddrKeyResolver();
    }
}
```

> 需要注意的是， 限流过滤器的优先级为0， 如果有的过滤器执行顺序为0或小于0，也就是优先级高于限流器， 如果先执行了，且转发了访问，则会躲过限流器，进而使限流器失效。



##### Gateway服务化

通过注册中心来调用服务，而不是通过硬编码的方式

###### 通过路径添加服务名前缀

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>gateway</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>gateway</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <!--        利用redis做限流 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
<!--        通过注册中心调用服务 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>
```

配置文件

```properties
eureka:
  client:
    service-url:
      default-zone: http://localhost:8761/eureka/


spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true  # 开启服务发现
          lower-case-service-id: true # 将请求路径上的服务名配置为小写
```

通过以上的配置，可以使得通过localhost:8085/eureka-client/hello?token=aaa去访问注册的服务， 其中eureka-client为服务名。

###### 通过负载均衡方式（路径无服务前缀）

1. 首先要将服务发现设为false，避免两种方式都可访问

2. 配置服务uri为 lb://服务名

3. 配置断言和过滤器

配置

```properties
eureka:
  client:
    service-url:
      default-zone: http://localhost:8761/eureka/


spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: false  # 开启服务发现
          lower-case-service-id: true # 将请求路径上的服务名配置为小写
      routes:
        - id: eureka-client
          uri: lb://EUREKA-CLIENT
          predicates:
            - Path=/foo/** # 符合/foo开头的路径,都进行映射
          filters:
            - StripPrefix=1 # 去掉/foo前缀,然后转发
```

然后可以通过http://localhost:8085/foo/hello?token=123进行访问了。



#### Consul - 另一个注册中心

同eureka一样，可以作为注册中心，实现服务的注册与发现，是分布式的。遵从RAFT算法保持一致性。

> RAFT算法：保证强一致性，意味着它需要领导者选举和集群协调来锁定服务。
>
> server中RAFT算法将它们分为三个角色，leader，follower，candidation（候选者）

##### Consul介绍

###### 术语

- 代理（Agent）：Consul服务集群中每个节点上的守护进程。通过Consul agent启动。可以扮演客户端或者服务端。用来通过DNS

  或HTTP来检查服务，保持同步。

  当代理请求的时候，就是客户端

  当代理接收请求的时候，就是服务端

- 客户端（Client）：一个代理，转发所有RPC到到服务端，加入LAN gossip池。

- 服务端（Server）：一个代理，分leader和follower，参与领导选举，响应RPC响应，与其他服务

- 数据中心（Data Center）：客户端和服务端组成的一个私有领域，具备低延迟高带宽的特征。

- 共识（Consensus）：当选领导者的协议，交易顺序的协议

- Gossip：serf的基础上的一个协议

- LAN Gossip：局域网的Gossip，包含一个局域网中所有的节点

- WAN Gossip：服务端的WAL Gossip池。

- 远程调用（RPC）：

###### Consul的特点和功能点

- 服务发现：客户端可以向服务端注册（通过DNS或HTTP）
- 运行时健康检查：这些检查机制
- KV存储：可以键值存储，通过HTTP API

###### **Consul的原理**

​	提供服务的节点运行Consul代理（Agent），运行代理时，代理只负责监控检查。

​	代理点可以和一个或多个Consul服务端通信。

​	Consul服务端时存储和复制数据的节点，采用RAFT算法保证了数据一致性。

​	Consul支持多个数据中心，建议每个数据中心的Consul server 集群化。

###### Consul的基本架构

​	Consul的基本架构一般时多数据中心，这在分布式系统中非常常见。

​	每个数据中心由客户端和服务端混合的。

​	一般建议奇数个服务器，如5台，3台。这是基于有故障情况下的可用性和性能之间的权衡的结果，越多的机器加入，达成共识就越慢

​	客户端的数量并不受限制。



​	同一个数据中心都加入Gossip协议，意味着Gossip协议池包含所有节点。这会带来一些好处：

	- 不需要客户端配置服务端地址，服务发现自动完成。
	- 检测节点故障不放在服务端，是分布式的。
	- 数据中心看可以被用做消息传递层，用以通知其他。



​    数字中心的每个服务端节点都是RAFT协议的一部分，共同选举一个领导者。

​	领导者负责查询和处理所有的事情，并同步复制给其他服务端。

​	非领导者接收RPC的时候，会转给领导者。



​	服务端也是WAN Gossip协议池的一部分。

​	这允许服务中心以低接触的方式发现并跨中心通信。

​	一个服务接收到跨中心请求，会转发到正确的服务中心的随机服务端，再由服务端转发到本地领导者处理。

​	

​	数据中心耦合度非常低，但是由于故障检测，连接缓存和多路复用等机制，跨数据中心请求相对快速。

###### Consul服务注册发现流程

​	其注册和发现过程和eureka相似

- 服务提供者（Provider）启动时，会向Consul发送一个请求，将其host，ip，应用名，健康检查等元数据信息发送给Consul。
- Consul接收到服务提供者的注册后，定期向其发送健康检查的请求，检验服务提供者是否健康。
- 服务消费者会从注册中心获取服务注册列表，当服务消费者消费服务的时候，会根据服务名从注册列表中获取具体服务的示例，再通过负载均衡策略完成服务的调用。

##### Consul与Eureka比较

| 特性             |      eureka      |      consul      |
| :--------------- | :--------------: | :--------------: |
| 一致性           | 弱一致性服务视图 | 强一致性服务视图 |
| spring Cloud首选 |       首选       |       备选       |
| 运行时健康检查   |      不支持      |    支持，丰富    |
| 键值存储         |      不支持      |       支持       |
| 多数据中心       |      不支持      |       支持       |
| 分布式状态复制   |      不支持      |       支持       |
| 一致性实现       |       心跳       |     raft算法     |

##### Spring Cloud Consul

Consul作为注册中心使用，与eureka几乎没有区别

###### Consul安装

官网下载

本个版本 consul --version 

Consul v1.9.0



```shell
# Consul启动 
consul agent -dev
```

> 还有更详细的选项，再次不做过多介绍

常用命令

| 命令    | 解释                             | 示例                |
| ------- | -------------------------------- | ------------------- |
| agent   | 运行一个consul agent             | consul  agent  -dev |
| join    | 将agent加入consul集群            | consul join IP      |
| members | 列出 consul cluster集群的members | consul member       |



###### 服务提供者

**pom**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqiujing.study</groupId>
    <artifactId>consul-provider</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>consul-provider</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.SR9</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```



**配置**

```properties
server:
  port: 8763
spring:
  application:
    name: consul-provider
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        serviceName: consul-prov
```

**代码**

```java
/**
 * @Author chengqj
 * @Date 2020/12/23 21:54
 * @Desc 服务提供者
 */
@RestController
public class ConsulController {
    @Value("${server.port}")
    String port;

    @GetMapping("/hi")
    public String home(@RequestParam String name){
        return "hi " + name + ",i am from port:" + port;
    }

}
```

###### 



###### 服务发现者

pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>consul-consumer</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>consul-consumer</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.SR9</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

配置

```properties
server:
  port: 8765
spring:
  application:
    name: consul-consumer
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        serviceName: consul-cons
```

代码

```java
/**
 * @Author chengqj
 * @Date 2020/12/23 22:07
 * @Desc  feign调用api
 */
@FeignClient(value = "consul-prov")
public interface HiFeignInterface {
    @GetMapping("/hi")
    String sayHiFromClient(@RequestParam(value="name") String name);
}

/**
 * @Author chengqj
 * @Date 2020/12/27 15:50
 * @Desc service层
 */
@Service
public class HiService {
    @Autowired
    HiFeignInterface hiFeign;

    public String sayHi(String name){
        return hiFeign.sayHiFromClient(name);
    }
    
}

/**
 * @Author chengqj
 * @Date 2020/12/27 16:11
 * @Desc Control层
 */
@RestController
public class ConsumerController {
    @Autowired
    HiService hiService;

    @Autowired
    private LoadBalancerClient loadBalancer;
    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/call")
    public String call(String name){
        return hiService.sayHi(name);
    }

    @RequestMapping("/call1")
    public String call1(String name){

        return hiService.sayHi(name);
    }
}
```

> 在我的版本中我遇到了一个问题：
>
> 就是当我不引入actuator作为健康检查时，远程服务时无法调用的，会报500，loadBalance无法发现服务。
>
> <dependency>
>             <groupId>org.springframework.boot</groupId>
>             <artifactId>spring-boot-starter-actuator</artifactId>
>         </dependency>
>
> 还有一个问题:
>
> 当Controller中的方法参数被@RequestParam修饰的时候，参数必须有，不可为null，不然会报400 bad request



##### Spring Cloud Consul Config 做配置中心

pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chenqj.study</groupId>
    <artifactId>consul-config</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>consul-config</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.SR9</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-config</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

启动类

开启发现服务

```java
@SpringBootApplication
@EnableDiscoveryClient
public class ConsulConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsulConfigApplication.class, args);
    }

}
```

java代码

开启自动刷新

```java
/**
 * @Author chengqj
 * @Date 2021/1/1 10:57
 * @Desc
 */
@RestController
@RefreshScope  // 开启自动刷新
public class FooBarController {
    @Value("${foo.say}")
    private String fooBar;

    @GetMapping("/foo")
    public String getFooBar(){
        return fooBar;
    }

}
```

properties -- bootstrap.yml

注意这里是bootstrap.yml

```properties
spring:
  application:
    name: consul-config
  cloud:
    consul:
      host: localhost
      port: 8500
      config:
        enabled: true
        prefix: configuration
        defaultContext: apps
        profileSeparator: '::'
        format: yaml
        data-key: dd
```



以下是对 consul.config部分常用属性的解释

配置主要分两部分

- 一部分是连接的配置，例如地址，配置项路径等
- 一部分是监控的设置，例如监控间隔，监控最长等待时间等

```properties
# consul 基本配置
spring.cloud.consul.host=127.0.0.1
spring.cloud.consul.port=8500

# 启用 consul 配置中心
spring.cloud.consul.config.enabled=true
# 基础文件夹，默认值 config
spring.cloud.consul.config.prefix=config
# 应用文件夹，默认值 application，consul 会加载 config/<applicationName> 和 config/<defaultContext> 两份配置，设置为相同值，则只加载一份
#spring.cloud.consul.config.default-context=testApp
spring.application.name=consul-config
# 环境分隔符，默认值 ","
spring.cloud.consul.config.profile-separator=':'
# 配置转码方式，默认 key-value，其他可选：yaml/files/properties
spring.cloud.consul.config.format=YAML
# 配置 key 值，value 对应整个配置文件
spring.cloud.consul.config.data-key=data
# 启用配置自动刷新
spring.cloud.consul.config.watch.enabled=true
# 【疑问】请求 consul api 的延迟，单位：秒
spring.cloud.consul.config.watch.wait-time=10
# 刷新频率，单位：毫秒
spring.cloud.consul.config.watch.delay=10000
```

consul配置项

登录consul控制页 (http://localhost:8500)，选择key/value，点击create，创建configuration/consul-config/dd，然后在文本框里填入与spring.cloud.consul.config.format=YAML属性格式的数据。这里是yaml格式的数据

```yaml
foo:
  bar: bar123
```

路径configuration/consul-config/dd的组成规则为 spring.cloud.consul.config.prefix/applicationName(或者spring.cloud.consul.config.default-context)[:spring:profiles:active]/spring.cloud.consul.config.data-key

> Q&A
>
> 这里踩坑颇多:
>
> - 首先必须统一版本, spring boot和spring cloud的版本请参照pom文件
> - 使用配置中心的方式是`<artifactId>spring-cloud-starter-consul-discovery</artifactId>`
>
> 统一以上两点后,才有确定问题的必要,否则环境不确定, 问题即便相同,引起的原因可能也不一样
>
> 1. 放在application.yml和application-dev.yml中consul作为配置中心,读取不到配置
>
>    因为 spring cloud consul 在需要在加载上下文的时候需要访问consul配置,所以需要将配置放到 bootstrap.yml中, 而且在spring cloud的官方文档中明确给出了提示,要求使用spring cloud consul config时,必须将配置放在bootstrap.yml配置文件中.
>
> 2. spring.cloud.consul.config.profile-separator=':'
>
>    这项配置可以写作 spring.cloud.consul.config.profile-separator='::' 都是可以的
>
> 3. 从spring boot 2.0+开始我们不需要在启动类上加@EnableAutoConfiguration
>
> 4. consul的配置spring.cloud.consul.config.enabled=true 这是默认，所以可以不配值
>
> 5. 如果本地设置很多配置不需要指定（在ConsulProperties有本地默认配置），我们可以只填写路径信息。也可以完全自定义设置，就像给出的例子，但这要遵循第一条。





#### 配置中心 - Spring Cloud Config

- Config Server从本地读取配置文件
- Config Server从远程Git仓库读取配置文件
- 搭建高可用Config Server集群
- 使用Spring Bus刷新配置

##### Config Server从本地读取配置文件

###### 构建Config Server

pom

只需要一个依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.8.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>config-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>config-server</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>
```

启动类

配置服务注解@EnableConfigServe

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

}
```

配置文件

application.yml

```yaml
server:
  port: 8769

spring:
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/shared
  profiles:
    active: native # 从本地读取，读取位置为search-locations
  application:
    name: config-server
```

同时需要在Resources目录下创建shared/config-client-dev.yml。shared目录由search-locations: classpath:/shared配置决定，config-client-dev.yml的规则为

[spring.application.name]-[spring.profiles.active].yml。用以被config-client访问。

###### 构建Config Client

pom

只需引入web依赖，spring cloud config client依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.8.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>config-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>config-client</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>
```

配置文件

必须是bootstrap.yml

```properties
spring:
  application:
    name: config-client
  cloud:
    config:
      uri: http://localhost:8769
      fail-fast: true
  profiles:
    active: dev
```

java类（启动类&实现）

启动类里包含配置信息测试，这里配置项不能刷新，还需要依靠别的组件（spring cloud bus）

```java
@RestController
@SpringBootApplication
public class ConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }


    @Value("${foo}")
    String foo;

    @RequestMapping("/foo")
    public String hi(){
        return foo;
    }
}
```



##### 从远程Git仓库读取配置文件

pom

引入 <artifactId>spring-cloud-config-server</artifactId>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.8.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>config-server-git</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>config-server-git</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>
```

配置文件

在配置文件中指定git地址

```properties
server:
  port: 8769
spring:
  cloud:
    config:
      server:
        git:
          uri: https://gitee.com/chengqiujing/testconfig
          search-paths: respo
          username: chengqiujing
          password: ***
      label: master
  application:
    name: config-server-git
```

uri ：git仓库地址

search-paths：git 仓库中目录地址

username：git用户名

password：用户密码

label：分支

> 仓库i地址目录下配置文件名，命名规则为[spring.application.name]-[spring.profiles.active].yml

 启动类

启动类指定配置中心注解@EnableConfigServer

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerGitApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerGitApplication.class, args);
    }

}
```



##### 构建高可用Config Server

Spring Cloud Config构建高可用是通过构建多服务实例注册到Eureka注册中心实现的。

###### 构建Config Server

pom

- eureka发现依赖
- config server服务依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.8.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>config-server-available</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>config-server-available</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>
```

配置

spring.profiles.active: native指定从本地读取配置文件，search-locations: classpath:/shared指定目录（在Resources下）

指定eureka

```properties
server:
  port: 8769

# active: native指定从本地读取配置文件，search-locations: classpath:/shared指定目录（在Resources下）
spring:
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/shared
  profiles:
    active: native
  application:
    name: config-server-avaible

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

启动类

```java
@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class ConfigServerAvailableApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerAvailableApplication.class, args);
    }

}
```

> 可以构建多个实例，客户端调用时可以负载均衡



###### 构建Config Client

pom

- web依赖
- Spring cloud config 客户端
- eureka客户端

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.8.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>config-client-avaible</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>config-client-avaible</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>
```

配置文件

这两个配置文件要按照此配置，不可混乱。eureka不可以放到bootstrap.yml中，其它配置也要按照分配配置

bootstrap.yml

以下配置需在bootstrap.yml中

```properties
spring:
  application:
    name: config-client-available
  cloud:
    config:
      #      uri: http://localhost:8769
      fail-fast: true
      discovery:
        enabled: true
        service-id: config-server-avaible
  profiles:
    active: dev
```

application.yml

以下配置需要在application.yml中

```properties
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

启动及测试

```java
@SpringBootApplication
@RestController
@EnableEurekaClient
public class ConfigClientAvaibleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientAvaibleApplication.class, args);
    }

    @Value("${foo}")
    String foo;

    @RequestMapping("/foo")
    public String hi() {
        return foo;
    }
}
```



##### 使用Spring Cloud Bus刷新配置

Spring Cloud Bus 就是一个轻量级的消息代理。可用于广播配置文件，或者服务的监控管理。

消息总线可以做微服务监控，亦可以实现程序通信。

Spring Cloud Bus的具体实现组件有RabbitMQ，AMQP和Kafka等。

消息组件的作用就是通知其它服务实例，获取配置



举例：当远程git更新后，可向某一个微服务实例发一个POST请求，**通过消息组件通知其它微服务实例重新拉取配置文件**。

###### 服务端参照git仓库



###### 客户端

pom

对当前版本如Spring Cloud Bus的依赖引入

- 需要引入<artifactId>spring-boot-starter-actuator</artifactId>
- 需引入总线消息代理<artifactId>spring-cloud-starter-bus-amqp</artifactId>
- 配置中心客户端基本依赖<artifactId>spring-boot-starter-web</artifactId>及<artifactId>spring-cloud-starter-config</artifactId>
- 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.8.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>config-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>config-client</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>
```

配置

bootstrap.xml

必须在此文件中

```yaml
spring:
  application:
    name: config-client
  cloud:
    config:
      uri: http://localhost:8769
      fail-fast: true
  profiles:
    active: dev
```

application.yml

必须在application.yml中

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin
management:
  endpoints:
    web:
      exposure:
        include: bus-refresh
```

> 这里需要注意：
>
> 当2.0以下 配置开放management.secutiry.enable=false来关闭验证
>
> 当2.0以上 配置为
>
> management:
>   endpoints:
>     web:
>       exposure:
>         include: bus-refresh
>
> 注意是 bus-refresh
>
> 

启动类及测试

配置@RefreshScope

当改变配置，推送到git，然后请求http://host:port/actuator/bus-refresh, 就可以出发刷新 

```java
@RestController
@SpringBootApplication
@RefreshScope
public class ConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }


    @Value("${foo}")
    String foo;

    @RequestMapping("/foo")
    public String hi() {
        return foo;
    }
}
```



##### 将配置存储在MySql中

配置从数据库读取

###### Config Server

**pom文件**

- jdbc
- mysql driven
- spring config server

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.8.BUILD-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>config-server-mysql</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>config-server-mysql</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.4</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </pluginRepository>
        <pluginRepository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>

</project>
```



**配置文件**

- 指定datasource相关属性

- 指定spring.profiles.active为jdbc
- 指定配置spring.cloud.config.label|server的相关属性，如下配置

```properties
spring:
  profiles:
    active: jdbc # 注意如果是从mysql中获取配置，则此处必须是jdbc
  application:
    name: config-jdbc-server
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/config-jdbc?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&serverTimezone=GMT%2B8
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  cloud:
    config:
      label: master  # 读取配置文件的分支
      server:
        jdbc:
          sql: SELECT key1,value1 from config_properties where APPLICATION=? and PROFILE=? and LABEL=?
server:
  port: 8769
```

> 对jdbc.sql的解释：
>
> SELECT key1,value1 from config_properties where APPLICATION=? and PROFILE=? and LABEL=?SELECT key1,value1 from config_properties where APPLICATION=? and PROFILE=? and LABEL=?
>
> 是根据**客户端**的 
>
> application.name=client-config
>
> profile.active=dev
>
> 还要根据server端的
>
> label=master



**启动类**

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerMysqlApplication.class, args);
    }

}
```



**生成mysql库的脚本**

```sql
CREATE TABLE `config_properties` (
`id` bigint(20) not null auto_increment,
`key1` varchar(50) collate utf8_bin not null,
`value1` varchar(500) collate utf8_bin default null,
`application` varchar(50) collate utf8_bin default null,
`profile` varchar(50)   collate utf8_bin not null,
`label` varchar(50) collate utf8_bin default null,
primary key(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 collate=utf8_bin;


INSERT INTO `config_properties` ( `id`, `key1`, `value1`, `application`, `profile`, `label` )
VALUES
   ( '1', 'server.port','8083', 'config-client', 'dev', 'master' );
INSERT INTO `config_properties` ( `id`, `key1`, `value1`, `application`, `profile`, `label` )
VALUES
   ( '2', 'foo','bar-jdbc', 'config-client', 'dev', 'master' );
```







#### 服务链路追踪 - Spring Cloud Sleuth

Spring Cloud负责链路追踪的Spring Cloud Sleuth模块，本次集成ZipKin

同类组件

- Google 的 Dapper
- Zipkin
- 阿里 的 Eagleeye（鹰眼）

链路追踪基本术语

- span： 一个工作单元，由一个64位id标识
- trace：一个请求产生的一系列span组成，成树形结构，也有一个64位id标识
- annotation：用于记录一些核心事件，例如一个开始或者结束

​       cs-Client Sent：请求的开始

​		sr-Server Received：服务端获得请求，sr-cs=网络请求时间

​		ss-Server Sent：服务端请求处理完成，并发送 ss-sr=服务端处理时间

​		cr-Client Received：客户端接收到，Span结束，请求完成。cr-cs=整个请求时间

ZipKin架构

- Collector：链路数据收集器，收集后转为Zipkin的span格式
- Storage：存储组件，可以存到Mysql，ElasticSearch中
- RESTful API：向开发者提供的API接口
- Web UI：展示组件



##### 注册中心

参考eureka注册中心段没有改动，可以照搬



##### 服务客户端

在eureka客户端基础上做出如下改动

###### pom

增加

```xml
<!--        zipkin      -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

###### 配置如下

```properties
server:
  port: 8080
spring:
  application:
    name: eureka-client
  sleuth:
    web:
      client:
        enabled: true
    sampler:
      probability: 1.0 # log collect percentage 1.0 is 100%
  zipkin:
    base-url: http://localhost:9411/ # zipkin server host
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

> 这个是存于内存，并于Zipkin服务直接通信



##### 服务调用端

可以参考feign调用

###### pom

增加

```xml
<!--        zipkin   -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

###### 配置

```properties
server:
  port: 8083
spring:
  application:
    name: feign-client
  sleuth:
    web:
      client:
        enabled: true
    sampler:
      probability: 1.0
  zipkin:
    base-url: http://localhost:9411 # zipkin server
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
feign:
  hystrix:
    enabled: true
management:
  endpoints:
    web:
      exposure:
        include: hystrix.stream,info,health
```





##### ZipKin

自从Spring Cloud Edgware版本后，ZipKin强制采用官方提供的jar包，同理也可以从github拉下来执行。

启动命令

> java -jar zipkin.jar
>
> zipkin的诸多配置是通过环境变量配置的，我们可以通过指定环境便来来配置。这里对环境变量不赘述了。请自行查阅。

默认端口 9411

可以直接访问 http://localhost:9411 查看ZipKin追踪数据

###### 自定义链路数据

```java
@Autowired
Tracer tracer;

@GetMapping("/feign")
// @HystrixCommand(fallbackMethod = "hiError")
public String get() {
    tracer.currentSpan().tag("name", "btbullle"); // 附加的自定义链路数据
    return helloCall.sayHello("这是Feign内容");
}
```

###### 传输

**直接传输**

直接传输就是配置中指定 base-url: http://localhost:9411 # zipkin server



**通过队列传输**

可以通过kafka或者rabbitMQ，这里我们使用rabbitMQ

基于docker启动rabbitMQ

> 1. docker pull  rabbitmq:3.7.17
> 2. docker run -dit --name Myrabbitmq -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 rabbitmq



需要在服务段或则客户端添加mq支持

pom增加

```xml
<!--    rabbitmq    -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit</artifactId>
</dependency>
```

配置文件增加rabbitmq的配置，并指定type: rabbit

```properties
server:
  port: 8080
spring:
  application:
    name: eureka-client
  sleuth:
    web:
      client:
        enabled: true
    sampler:
      probability: 1.0 # log collect percentage 1.0 is 100%
#  zipkin:
#    base-url: http://localhost:9411/ # zipkin server host
  zipkin:
    sender:
      type: rabbit
  rabbitmq:
    host: localhost
    username: admin
    password: admin
    port: 5672
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

并在Zipkin启动的时候加入环境变量

```shell
java -jar .\zipkin-server-2.12.9-exec.jar --zipkin.collector.rabbitmq.addresses=localhost:5672 --zipkin.collector.rabbitmq.username=admin --zipkin.collector.rabbitmq.password=admin
```

**Q&A**

访问rabbitMQ的网页打不开？

是因为rabbitMQ（我是docker运行的）的网页运行需要启动插件，我们可以进入容器，执行命令 `rabbitmq-plugins enable rabbitmq_management`来开启插件



###### 存储于内存

存储与内存就是不配置任何存储组件



###### 存储于mysql

首先，启动zipkin需要指定mysql的环境变量

Mysql

```shell
java -jar .\zipkin-server-2.12.9-exec.jar --zipkin.collector.rabbitmq.addresses=localhost:5672 --zipkin.collector.rabbitmq.username=admin --zipkin.collector.rabbitmq.password=admin --zipkin.storage.type=mysql --zipkin.storage.mysql.host=localhost --zipkin.storage.mysql.port=3306 --zipkin.storage.mysql.username=root --zipkin.storage.mysql.password=root
```

建表脚本，建表

```sql
--
-- Copyright 2015-2019 The OpenZipkin Authors
--
-- Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
-- in compliance with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software distributed under the License
-- is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
-- or implied. See the License for the specific language governing permissions and limitations under
-- the License.
--

CREATE TABLE IF NOT EXISTS zipkin_spans (
  `trace_id_high` BIGINT NOT NULL DEFAULT 0 COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` BIGINT NOT NULL,
  `id` BIGINT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `remote_service_name` VARCHAR(255),
  `parent_id` BIGINT,
  `debug` BIT(1),
  `start_ts` BIGINT COMMENT 'Span.timestamp(): epoch micros used for endTs query and to implement TTL',
  `duration` BIGINT COMMENT 'Span.duration(): micros used for minDuration and maxDuration query',
  PRIMARY KEY (`trace_id_high`, `trace_id`, `id`)
) ENGINE=InnoDB ROW_FORMAT=COMPRESSED CHARACTER SET=utf8 COLLATE utf8_general_ci;

ALTER TABLE zipkin_spans ADD INDEX(`trace_id_high`, `trace_id`) COMMENT 'for getTracesByIds';
ALTER TABLE zipkin_spans ADD INDEX(`name`) COMMENT 'for getTraces and getSpanNames';
ALTER TABLE zipkin_spans ADD INDEX(`remote_service_name`) COMMENT 'for getTraces and getRemoteServiceNames';
ALTER TABLE zipkin_spans ADD INDEX(`start_ts`) COMMENT 'for getTraces ordering and range';

CREATE TABLE IF NOT EXISTS zipkin_annotations (
  `trace_id_high` BIGINT NOT NULL DEFAULT 0 COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` BIGINT NOT NULL COMMENT 'coincides with zipkin_spans.trace_id',
  `span_id` BIGINT NOT NULL COMMENT 'coincides with zipkin_spans.id',
  `a_key` VARCHAR(255) NOT NULL COMMENT 'BinaryAnnotation.key or Annotation.value if type == -1',
  `a_value` BLOB COMMENT 'BinaryAnnotation.value(), which must be smaller than 64KB',
  `a_type` INT NOT NULL COMMENT 'BinaryAnnotation.type() or -1 if Annotation',
  `a_timestamp` BIGINT COMMENT 'Used to implement TTL; Annotation.timestamp or zipkin_spans.timestamp',
  `endpoint_ipv4` INT COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_ipv6` BINARY(16) COMMENT 'Null when Binary/Annotation.endpoint is null, or no IPv6 address',
  `endpoint_port` SMALLINT COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_service_name` VARCHAR(255) COMMENT 'Null when Binary/Annotation.endpoint is null'
) ENGINE=InnoDB ROW_FORMAT=COMPRESSED CHARACTER SET=utf8 COLLATE utf8_general_ci;

ALTER TABLE zipkin_annotations ADD UNIQUE KEY(`trace_id_high`, `trace_id`, `span_id`, `a_key`, `a_timestamp`) COMMENT 'Ignore insert on duplicate';
ALTER TABLE zipkin_annotations ADD INDEX(`trace_id_high`, `trace_id`, `span_id`) COMMENT 'for joining with zipkin_spans';
ALTER TABLE zipkin_annotations ADD INDEX(`trace_id_high`, `trace_id`) COMMENT 'for getTraces/ByIds';
ALTER TABLE zipkin_annotations ADD INDEX(`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames';
ALTER TABLE zipkin_annotations ADD INDEX(`a_type`) COMMENT 'for getTraces and autocomplete values';
ALTER TABLE zipkin_annotations ADD INDEX(`a_key`) COMMENT 'for getTraces and autocomplete values';
ALTER TABLE zipkin_annotations ADD INDEX(`trace_id`, `span_id`, `a_key`) COMMENT 'for dependencies job';

CREATE TABLE IF NOT EXISTS zipkin_dependencies (
  `day` DATE NOT NULL,
  `parent` VARCHAR(255) NOT NULL,
  `child` VARCHAR(255) NOT NULL,
  `call_count` BIGINT,
  `error_count` BIGINT,
  PRIMARY KEY (`day`, `parent`, `child`)
) ENGINE=InnoDB ROW_FORMAT=COMPRESSED CHARACTER SET=utf8 COLLATE utf8_general_ci;
```

然后，就可以测试了。



###### 存储于ElasticSearch

1. 首先，拉取es的镜像

   ```shell
   docker pull elasticsearch:6.8.10
   ```

2. 启动命令

   ```shell
   docker run -d -e ES_JAVA_POTS="-Xms128m -Xmx128m"  -e "discovery.type=single-node" -p 9200:9200 -p 9300:9300 --name es elasticsearch:6.8.10
   ```

3. zipkin启动命令

   ```shell
   java -jar .\zipkin-server-2.12.9-exec.jar --zipkin.collector.rabbitmq.addresses=localhost:5672 --zipkin.collector.rabbitmq.username=admin --zipkin.collector.rabbitmq.password=admin --zipkin.storage.type=elasticsearch --zipkin.storage.elasticsearch.hosts=localhost:9200 --zipkin.storage.elasticsearch.index=zipkin
   ```

   

   ###### Q&A

   1.docker启动时ES只运行几秒，便推出。报错：max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]

   ​	这个需要去修改docker的一个配置文件。地址：https://blog.csdn.net/weixin_35726962/article/details/109800916

   windows wsl下

   ```shell
   
   wsl -d docker-desktop
   
   sysctl -w vm.max_map_count=262144
   ```

​     2.docker启动时ES报错：the default discovery settings are unsuitable for production use; at least one of [discovery.seed_hosts, discovery.seed_providers, cluster.initial_master_nodes] must be configured

​		这是指启动docker时，需要指定一个参数，` -e "discovery.type=single-node"` ，这个参数在启动时已经被加上。

​    3.Zipkin登录页面时报错，和index有关，（auto.create...）

​	这是因为ES版本和Zipkin版本不匹配造成的，需要调整版本，本文档版本已经试验过，可以使用。

###### 基于Kibana展示

拉取镜像

```shell
docker pull kibana:6.8.10
```

启动

```shell
docker run --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.124.100:9200 -p 5601:5601 -d kibana:7.10.1
```



> docker国内镜像加速网站
>
> ```json
> "https://registry.docker-cn.com",
> "http://hub-mirror.c.163.com",
> "https://docker.mirrors.ustc.edu.cn"
> ```



#### 微服务监控 - Spring Boot Admin

Spring Boot Acutor是监控一个Spring Boot项目。而Spring Boot Admin用于管理和监控一个或者多个Spring Boot程序。

Spring Boot Adminfeng为Server（服务端）和Client（客户端）

客户端可以直接向服务端注册，也可以通过注册中心向其进行注册

Spring Boot Admin提供了React编写的UI界面

###### Spring Boot Admin监控Spring Boot项目

**server端**

pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>admin-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>admin-server</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-boot-admin.version>2.3.1</spring-boot-admin.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-dependencies</artifactId>
                <version>${spring-boot-admin.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

配置application.yml

```properties
spring:
  application:
    name: admin-server
server:
  port: 8769
```

启动类配置

```java
@SpringBootApplication
@EnableAdminServer
public class AdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }

}
```



**client端**

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>admin-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>admin-client</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
        <spring-boot-admin.version>2.3.1</spring-boot-admin.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-dependencies</artifactId>
                <version>${spring-boot-admin.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

配置 application.yml

```yml
spring:
  application:
    name: admin-client
  boot:
    admin:
      client:
        url: http://localhost:8769
server:
  port: 8768
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always
```

启动类不需要配置



先启动server 再启动client 然后访问 http://localhost:8769，就可以看到界面



###### Spring Boot Admin监控Spring Cloud项目

**eureka-server**

请参照之前的eureka server例子

**admin-client-cloud**

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>admin-client-cloud</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>admin-client-cloud</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>2020.0.0</spring-cloud.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
    </repositories>

</project>
```

配置（application.yml）

```properties
spring:
  application:
    name: admin-client-cloud
eureka:
  client:
    registry-fetch-interval-seconds: 5
    service-url:
      defaultZone: ${EUREKA_SERVICE_URL:http://localhost:8761}/eureka/
  instance:
    lease-renewal-interval-in-seconds: 10
    health-check-url-path: /actuator/health
server:
  port: 8768
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always
```

启动类

```java
@SpringBootApplication
@EnableEurekaClient
public class AdminClientCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminClientCloudApplication.class, args);
    }

}
```



**admin-server-cloud**

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>admin-server-cloud</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>admin-server-cloud</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
        <spring-boot-admin.version>2.3.1</spring-boot-admin.version>
        <spring-cloud.version>2020.0.0</spring-cloud.version>
    </properties>
    <dependencies>
        <!--        eureka -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-dependencies</artifactId>
                <version>${spring-boot-admin.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
    </repositories>

</project>
```

配置（application.yml）

```properties
spring:
  application:
    name: admin-server-cloud
server:
  port: 8769
eureka:
  client:
    registry-fetch-interval-seconds: 5
    service-url:
      defaultZone: ${EUREKA_SERVICE_URL:http://localhost:8761}/eureka/
  instance:
    lease-renewal-interval-in-seconds: 10
    health-check-url-path: /actuator/health
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always
```

启动类

```java
@SpringBootApplication
@EnableAdminServer
@EnableEurekaClient
public class AdminServerCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServerCloudApplication.class, args);
    }

}
```



###### Spring Boot Admin中添加Security 组件

因为Spring boot Admin暴露了太多的服务器信息，所以要控制

在上述的Admin-server的pom中添加

```xml
<!--        用来限制admin网页被随便访问-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
```



配置文件如下：

```properties
spring:
  application:
    name: admin-server-cloud
# 配置security用户名和密码    
  security:
    user:
      name: "admin"
      password: "admin"
server:
  port: 8769
eureka:
  client:
    registry-fetch-interval-seconds: 5
    service-url:
      defaultZone: ${EUREKA_SERVICE_URL:http://localhost:8761}/eureka/
  instance:
    lease-renewal-interval-in-seconds: 10
    health-check-url-path: /actuator/health
# 配置security用户名和密码  
    metadata-map:
      user.name: ${spring.security.user.name}
      user.password: ${spring.security.user.password}
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always
```

Spring Security 配置类

```java
package com.chengqj.study.adminservercloud.config.security;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * @Author chengqj
 * @Date 2021/1/23 9:54
 * @Desc
 */
@Configuration
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties){
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        http.authorizeRequests().antMatchers(adminContextPath + "/assets/**").permitAll()
                .antMatchers(adminContextPath + "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler)
                .and()
                .logout().logoutUrl(adminContextPath + "/logout")
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
```

然后访问：http://localhost:8769, 然后输入用户名和密码



###### Spring Boot Admin中添加Mail组件

在上节的基础上改动

Spring Boot Admin组件整合了mail，所以当引入依赖并加以配置后，就可以在client端挂掉后，发送通知到指定邮箱

pom.xml

增加

```xml
<!--        通过mail给我发消息-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
```

application.yml

增加以下配置

```yaml
spring:
  application:
    name: admin-server-cloud
  mail: # 邮箱相关配置
    host: smtp.163.com
    username: wowcheng163@163.com
    password: q125600183!@#
    protocol: smtp
    test-connection: true
    default-encoding: UTF-8
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
      mail.smtp.starttls.required: true
      mail.smtp.ssl.enable: true
  boot:
    admin:
      notify:
        mail:
          to: wowcheng163@163.com # 要发送的邮箱
          from: wowcheng163@163.com  # 来自哪个邮箱，必须和username项一致
```

配置完后，启动上节eureka server，admin server，admin client，然后停掉client，会有email发送到指定邮箱

> 注意这里
>
> from: wowcheng163@163.com  # 来自哪个邮箱
>
> 必须和username项一致，否则报错553 



#### Spring Boot Security 详解

##### 理论

安全包含两个领域：认证和授权

- 认证：你是谁
- 授权：能执行什么操作，能访问什么页面



##### 特点

Spring Security看可以在Controller层，Service层，DAO层等以加注解的方式来控制安全。

Spring Security提供了细粒度的权限控制。

精细到API接口，每一个业务方法，或者每一个操作数据库的DAO层方法。

Spring Security是应用程序层的安全解决方案，一个系统的安全还需要考虑传输层和系统层的安全。例如，https，服务器防火墙，集群隔离部署等。



Apache Shiro一般用以单体服务。

Spring Security服务于微服务。



##### Spring Security提供的安全模块

好多

好多

。。。

自定查询



##### Spring Boot Security与Spring security的关系

Spring Security主要包含以下两个jar，分别是spring-security-web依赖和spring-security-config依赖：

```xml
<dependencies>
    <dependence>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-web</artifactId>
        <version>4.2.2.RELEASE</version>
    </dependence>
    <dependence>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-config</artifactId>
        <version>4.2.2.RELEASE</version>
    </dependence>
</dependencies>
```

Spring Boot对Spring Security框架做了封装，仅仅是封装

```xml
	<dependence>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependence>
```



##### 构建Spring Boot Security工程

###### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.8.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>security</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.thymeleaf.extras</groupId>
            <artifactId>thymeleaf-extras-springsecurity5</artifactId>
            <version>3.0.4.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

###### 配置WebSecurityConfigurerAdapter

开启WebSecurity功能，指定基本认证信息和权限信息

```java
/**
 * @Author chengqj
 * @Date 2021/1/24 13:32
 * @Desc 这个类指定了认证信息和认证规则
 *
 * 这个类指定的工作
 * 1 用用的每一个请求都需要认证
 * 2 自动生成了一个登录表单
 * 3 可以用username和password来进行认证
 * 4 用户可以注销
 * 5 阻止了CSRF攻击
 * 6 Session Fixation 保护
 * 7 安全Header集成了以下内容
 * 	 ... ...
 * 8 集成了以下的servlet API方法
 *	HttpServletRequest#getRemoteUser()
 *	HttpServletRequest#getUserPrincipal()
 *	HttpServletRequest#isUserRole(java.lang.String)
 *	HttpServletRequest#login(String,String)
 *	HttpServletRequest#logout
 * */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication() // 内存存储
                .passwordEncoder(new BCryptPasswordEncoder()) // 指定密码加密器
                .withUser("forezp") // 指定用户名
                .password(new BCryptPasswordEncoder().encode("123456")) // 指定用户密码并加密
                .roles("USER"); // 指定用户角色
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**","/index").permitAll() // 这两个路径下的资源不需要验证
                .antMatchers("/user/**").hasRole("USER") // /user路径下需要角色USER
                .antMatchers("/blogs/**").hasRole("USER") // /blogs路径下需要角色USER
                .and()
                .formLogin().loginPage("/login") // 表单登录地址是/login
                .failureUrl("/login-error") // 登录失败的地址是/login-error
                .and()
                .exceptionHandling().accessDeniedPage("/401"); // 异常处理重定向到/401
        http.logout().logoutSuccessUrl("/");
    }

}
```

###### Controller

```java
/**
 * @Author chengqj
 * @Date 2021/1/24 14:13
 * @Desc 模拟资源类
 */

@Controller
public class DemoController {
    @RequestMapping("/")
    public String root(){
        return "redirect:index";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/user/index")
    public String userIndex(){
        return "user/index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError", true);
        return "login";
    }

    @RequestMapping("/401")
    public String accessDenied(){
        return "401";
    }

}
```

###### application.yml

```yaml
spring:
  thymeleaf:
    mode: HTML5
    encoding: utf-8
    cache: false
```

###### 页面

login.html

```html
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Login page</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="/css/main.css" th:href="@{/css/main.css}">
</head>
<body>
    <h1>Login page</h1>
<p>User 角色用户:forezp/123456</p>
<p>Admin 角色用户:admin/123456</p>
<p th:if="${loginError}" class="error">用户名或密码错误</p>
<form th:action="@{/login}" method="post">
    <lable for="username">用户名:</lable>
    <input type="text" id="username" name="username" autofocus="autofocus">
    <label for="password">密码:</label>
    <input type="password" id="password" name="password" /><br>
    <input type="submit" value="登录">
</form>
<p><a href="/index" th:href="@{/index}">返回首页</a></p>
</body>
</html>
```

index.html

```html
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4">
<head>
    <meta charset="UTF-8">
    <title>Hello Spring Security</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="/css/main.css" th:href="@{/css/main.css}">
</head>
<body>
<h1>Hello Spring Security</h1>
<p>这个页面没有被保护,可以进入已被保护的页面</p>
<div th:fragment="logout" sec:authorize="isAuthenticated()">
    登录名:<span sec:authentication="name"></span>|
    用户角色:<span sec:authentication="principal.authorities"></span>
    <div>
        <form action="#" th:action="@{/logout}" method="post">
            <input type="submit" value="登出">

        </form>
    </div>
</div>
<ul>
    <li>点击<a href="/user/index" th:href="@{/user/index}">去/user/index已被保护的界面</a></li>
</ul>
</body>
</html>
```

401.html

```html
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4">
<body>
    <div/>
    <div>
        <h2>权限不够</h2>
    </div>
        <div sec:authorize="isAuthenticated()">
            <p>已有用户登录</p>
            <P>用户:<span sec:authentication="name"></span></P>
            <p>角色:<span sec:authentication="principal.authorities"></span></p>
        </div>
<div sec:authorize="isAnonymous()">
    <p>未有用户登录</p>
</div>
<p> 拒绝访问!</p>
</body>
</html>
```

/user/index.html，只有USER角色才能访问

```
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Login page</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="/css/main.css" th:href="@{/css/main.css}">
</head>
<body>
   <div th:substituteby="index::logout"></div>
<h1>这个界面是被USER保护的界面</h1>
<p><a href="/index" th:href="@{/index}">返回首页</a> </p>
<p><a href="/blogs" th:href="@{/blogs}">管理博客</a> </p>
</body>
</html>
```

###### 检验流程

1. 访问localhost：8080，会被重定向到localhost:8080/index
2. 单击页面的去/user/index，跳转到/login页面
3. 登陆后，进入USER角色才能访问的/user/index

###### 创建新角色

```java
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // ... 省略代码 ...

        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("ADMIN");
    }

   // ... 省略代码,参见之前 ...
}
```

###### 赋予新角色

直接添加 “USER”

```java
// ... 省略代码 ...
auth.inMemoryAuthentication()
        .passwordEncoder(new BCryptPasswordEncoder())
        .withUser("admin")
        .password(new BCryptPasswordEncoder().encode("123456"))
        .roles("ADMIN","USER");
```

###### 方法级别控制

**步骤**：

1. 配置开启注解 `@EnableGlobalMethodSecurity(prePostEnabled = true)`

   注解参数：

  - @PrePostEnabled 指定了@PreAuthorize和@PostAuthorize注解是否可用
  - @SecureEnabled 指定了Spring Security的@Secured注解是否可用
  - @Jsr250Enabled 指定了Spring Security对于JSR-250的注解是否可用


2. 为方法添加注解

   注解有：

   @PreAuthorize("hasAuthority('ROLE_ADMIN')")

   @PreAuthorize("hasAnyRole('ADMIN','USER')") 也可以写做@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")

**开启注解**

@EnableGlobalMethodSecurity(prePostEnabled = true)

```java
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别 ***
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication() // 内存存储
                .passwordEncoder(new BCryptPasswordEncoder()) // 指定密码加密器
                .withUser("forezp") // 指定用户名
                .password(new BCryptPasswordEncoder().encode("123456")) // 指定用户密码并加密
                .roles("USER"); // 指定用户角色

        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("ADMIN","USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/index").permitAll() // 这两个路径下的资源不需要验证
                .antMatchers("/user/**").hasRole("USER") // /user路径下需要角色USER
                .antMatchers("/blogs/**").hasRole("USER") // /blogs路径下需要角色USER
                .and()
                .formLogin().loginPage("/login") // 表单登录地址是/login
                .failureUrl("/login-error") // 登录失败的地址是/login-error
                .and()
                .exceptionHandling().accessDeniedPage("/401"); // 异常处理重定向到/401
        http.logout().logoutSuccessUrl("/");
    }

}
```

**为方法添加注解**

@PreAuthorize("hasAuthority('ROLE_ADMIN')")

```java
@GetMapping
public ModelAndView list(Model model){
    List<Blog> list = blogService.getBlogs();
    model.addAttribute("blogsList",list);
    return new ModelAndView("blogs/list", "blogModel",model);
}

@PreAuthorize("hasAuthority('ROLE_ADMIN')") // 表示只有ADMIN角色才可以访问这个方法
@GetMapping(value = "/{id}/deletion")
public ModelAndView delete(@PathVariable("id") Long id,Model model){
    blogService.deleteBlog(id);
    model.addAttribute("blogsList", blogService.getBlogs());
    return new ModelAndView("blogs/list","blogModel",model);
}
```

**其它类（辅助功能实现）**

```java
/**
 * @Author chengqj
 * @Date 2021/1/27 23:41
 * @Desc
 */
public interface IBlogService {
    List<Blog> getBlogs();
    void deleteBlog(long id);
}

/**
 * @Author chengqj
 * @Date 2021/1/27 23:45
 * @Desc
 */
@Service
public class BlogService implements IBlogService {
    private List<Blog> list = new ArrayList<>();

    public BlogService() {
        list.add(new Blog(1L, "Spring in action", "good!"));
        list.add(new Blog(2L, "Spring boot in action", "nice!"));
    }

    @Override
    public List<Blog> getBlogs() {
        return list;
    }

    @Override
    public void deleteBlog(long id) {
        Iterator<Blog> iterator = list.iterator();
        while (iterator.hasNext()){
            Blog blog = (Blog) iterator.next();
            if (blog.getId() == id) {
                iterator.remove();
            }
        }
    }
}

/**
 * @Author chengqj
 * @Date 2021/1/27 23:32
 * @Desc
 */
public class Blog {
    private Long id;
    private String name;
    private String content;

    public Blog(Long id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


```

###### 从数据库中读取认证信息

数据非常多时需要保存在数据库中。

**环境**

- mysql
- JPA

**步骤：**

1. 指定依赖pom

2. Entity操作 --》 Role，User 

   User实现接口`User implements UserDetails, Serializable`

   Role实现接口`Role implements GrantedAuthority`

3. service层操作

   service继承接口`public class UserService implements UserDetailsService`

4. 配置里边指定从数据库获取数据

**pom.xml**

```xml
<!--mysql-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
<!--jpa-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

**Java配置类**

```java
/**
 * @Author chengqj
 * @Date 2021/1/24 13:32
 * @Desc 这个类指定了认证信息和认证规则
 * <p>
 * 这个类指定的工作
 * 1 用用的每一个请求都需要认证
 * 2 自动生成了一个登录表单
 * 3 可以用username和password来进行认证
 * 4 用户可以注销
 * 5 阻止了CSRF攻击
 * 6 Session Fixation 保护
 * 7 安全Header集成了以下内容
 * ...
 * 8 集成了以下的servlet API方法
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;

    /**
     * 认证
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService) // 指定数据库
            // 这里在我的版本下，必须指定这个密码加密类，不然报错
            // 版本可以参考上边构建工程的版本
                .passwordEncoder(NoOpPasswordEncoder.getInstance()); 
        

     
    }

    /**
     * 配置资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/index").permitAll() // 这两个路径下的资源不需要验证
                .antMatchers("/user/**").hasRole("USER") // /user路径下需要角色USER
                .antMatchers("/blogs/**").hasRole("USER") // /blogs路径下需要角色USER
                .and()
                .formLogin().loginPage("/login") // 表单登录地址是/login
                .failureUrl("/login-error") // 登录失败的地址是/login-error
                .and()
                .exceptionHandling().accessDeniedPage("/401"); // 异常处理重定向到/401
        http.logout().logoutSuccessUrl("/");
    }

}
```

**其它java类**

```java
/**
 *
 * @package: com.chengqj.study.security.dao
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/6 16:35
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

/**
 *
 * @package: com.chengqj.study.security.service
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/6 16:42
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserDao userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}

/**
 * <p>
 * 注意：这个类实现了接口UserDetails, Serializable
 * UserDetails这个类是配合Spring Security认证信息的核心接口
 * 这是一个框架接口
 *
 * 其中username可以是名称,也可以是手机号,或邮件
 * 控制权限的可以是角色,也可以是用户其它信息
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description: User实体类
 * @author: chengqj
 * @date: Created in 2021/2/6 15:40
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
@Data
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false,unique = true)
    private String username;23r44444444444431111111111111111111111111111111111111111111

    @Column
    private String password;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private List<Role> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /**
         * 可以返回角色,也可以返回其它的条件
         */
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        /**
         * 这里可以返回username,也可以返回收集号码或者邮箱地址
         */
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

/**
 * <p>
 * 这个类继承了GrantedAuthority,这个类也是一个框架类
 *
 * getAuthority()可以返回角色名字符串,也可以是别的信息.
 * 这里的权限点是角色名
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/6 16:07
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @Override
    public String getAuthority() {
        return name;
    }
}
```

**附：建表SQL**

```sql
CREATE DATABASE spring-security DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

DROP TABLE `role`;
CREATE TABLE `role`(
    `id` bigint() NOT NULL AUTO_INCREMENT,
    `name` varchar(255) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`(
    `id` bitint(20) NOT NULL AUTO_INCREMENT,
    `password` varchar(255) COLLATE  utf8_bin DEFAULT NULL,
    `username` varchar(255) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY(`id`),
    UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
     `user_id` bigint(20) NOT NULL,
     `role_id` bigint(20) NOT NULL,
     KEY `FKAkdfjlandgu7a987932r` (`role_id`),
     KEY `FK987asdf9d8gu0ad0f8f` (`user_id`),
     CONSTRAINT `83762tr73t4ty9` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
     CONSTRAINT `8u4t9393hy34u` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```





##### 总结

![](D:\chengqj\Documents\typora\image\img.png)

对Spring Security的使用主要有@EnableWebSecurity和@EnableGlobalMethodSecurity两中方式，第一种方式是全局控制，第二种是方法级控制。

这又分内存级存储控制和数据库存储控制。

#### 使用Spring Cloud OAuth2 保护系统

OAuth2是一个标准的授权协议

这里推荐阮一峰老师的一篇文章，文章全面介绍了OAuth2（ https://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html ）

在OAuth2中分简化三种角色：资源持有者，授权服务器，资源服务器，还有就是客户端

![](D:\chengqj\Documents\typora\image\oauth2.png)

**简单解释**

##### 四个角色解决的问题

|      角色      |  角色名  | 解决的问题                                                   |
| :------------: | :------: | :----------------------------------------------------------- |
|   资源持有者   |   man    | 1. 拥有对资源的所有权<br />2. 浏览器或者第三方对资源的访问需要其显示授权 |
|   鉴权服务器   | Provider | 1. 维护ClientId并验证，用以识别可鉴权的请求<br />2. 授权码生成和验证<br />3. token生成和验证，以及管理（保存方式）<br />4. 显示的授权页面,用以显示给资源持有者<br />5. man的认证 |
|   资源服务器   | Resource | 1. 指定资源id<br />2. 怎么处理token编码，解码token校验（请求传入） |
| 浏览器，第三方 |  Client  | 1. 请求鉴权服务器,获取token<br />2. 每个资源服务器持有一个clientId和密码，用以<br />3. 未认证重定向认证 |

##### **OAuth2协议种有4种授权方式**

- 授权码模式（authorization code）
- 简化模式（implicit）
- 密码模式（resource owner password credentials）
- 客户端模式（client credentials）

OAuth2的具体流程，在这里不赘述，明者自明。

##### Spring Cloud OAuth2的实现

OAuth2协议在Spring Resource的实现为Spring OAuth2.

Spring OAuth2分为两部分，两部分包含对各个角色的实现

###### **OAuth2 Provider**

**》》》Authorization Server**：通过@EnableAuthorizationServer

- ClientDetailsServiceConfigurer：客户端信息（Provider - 1）
  - clientId：客户端的id
  - secret：客户端的密码
  - scope：客户端的域
  - authorizedGrantType：认证类型
  - authorities：权限信息
- AuthorizationServerEndpointsConfigurer:  授权节点（检验token）和获取token的节点（Provider - 3，Provider - 2），认证（Provider - 2）
  - authenticationManager：配置该项开启密码验证，或者其它验证方式（Provider - 2）
  - userDetailsService：管理认证信息（Provider - 2）
  - authorizatioinCodeServices：配置验证码（Provider - 2）
  - implicitGrantService：管理implict验证的状态
  - tokenGranter：配置Token Granter（Provider - 3）
    - InMemeoryToken：内存保存
    - JdbcTokenStore：数据库保存
    - JwtTokenStore：采用JWT形式
- AuthorizatioinServerSecurityConfigurer:  保护暴露的token节点（用以校验Provider - 1）
  - 基于spring security

**》》》Resource Server** ：用@EnableResourceServer

ResourceServerConfigurer：

- resourceId：资源Id
- tokenSrvices：定义Token的处理方式
  - ResourceServerTokenService：配置token是如何编码解码的，或是远程验证（Resource - 1）
  - RemoteTokenService：如果不在一个服务器，指定远程token（Resource - 1）

###### **OAuth2 Client** 远程验证

**》》》 Protected Resource Configuration**

- Id：资源ID，Spring Oauth2没有使用
- clientId：（Client - 2）
- clientSecret：（Client - 2）
- accessTokenUri：获取token的url（Client - 1）
- scope：对应鉴权服务的域
- clientAuthenicationScheme：客户端验证类型
  - Http Basic
  - Form
- userAuthorizationUri：未认证重定向到认证

**》》》 Client Configuration**：可以用@EnableOAuth2Client来简化配置

- 过滤器Bean（Bean的Id为oauth2ClientContextFilter）用以储存当前请求和上下文的请求，若需要认证则重定向
- AccessTokenRequest：在Request域内创建AccessTokenRequest类型的Bean

###### 代码实现

**Provider**

首先auth服务是依赖于Spring Security提供的认证功能的，其中Spring Security对于认证信息的存储及处理有一套自有体系。如下：

Spring Security认证

entity -- 实例同时实现了Spring Security认证所需的接口

```java
/**
 * <p>
 * 这个类继承了GrantedAuthority,这个类也是一个框架类
 * <p>
 * getAuthority()可以返回角色名字符串,也可以是别的信息.
 * 这里的权限点是角色名
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/6 16:07
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
public class Role implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;


  @Override
  public String getAuthority() {
    return name;
  }
}

/**
 * <p>
 * 注意：这个类实现了接口UserDetails, Serializable
 * UserDetails这个类是配合Spring Security认证信息的核心接口
 * 这是一个框架接口
 * <p>
 * 其中username可以是名称,也可以是手机号,或邮件
 * 控制权限的可以是角色,也可以是用户其它信息
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description: User实体类
 * @author: chengqj
 * @date: Created in 2021/2/6 15:40
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
@Data
public class User implements UserDetails, Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column(nullable = false, unique = true)
  private String username;

  @Column
  private String password;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private List<Role> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    /**
     * 可以返回角色,也可以返回其它的条件
     */
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    /**
     * 这里可以返回username,也可以返回收集号码或者邮箱地址
     */
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}

```

dao

```java
/**
 * @Author chengqj
 * @Date 2021/1/1 10:17
 * @Desc
 */
public interface UserDao extends JpaRepository<User, Long> {
  User findByUsername(String username);
}
```

service

```java
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
}
```

以及Spring Security的配置

1. 指定了验证请求范围

2. 配置了密码加密方式和密码存储方式
3. 配置验证管理的bean

```java
/**
 * @Author chengqj
 * @Date 2021/1/1 10:09
 * @Desc 1.configure(HttpSecurity http) 用来隔绝资源
 * 2.configure(AuthenticationManagerBuilder auth) 配置认证方式
 * 3.authenticationManagerBean()
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private UserServiceDetail userServiceDetail;

  /**
   * HttpSecurity中配置了所有的请求都需要安全验证
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().authenticated()
            .and()
            .csrf().disable();

  }

  /**
   * 配置了用户信息源和密码加密的策略
   * ps: 密码验证只有配置才会开启
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userServiceDetail).passwordEncoder(new BCryptPasswordEncoder()); // 配置从数据库获取密码
  }

  /**
   * 配置验证管理的Bean
   */
  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
```

> 在这里需要做出一定的解释：
>
> 以上基于Spring Security的认证措施，旨在隔离、保护资源。在授权服务器上，之所以需要此番作为，是因为认证的接口也是资源，也不是谁想访问就访问的。所以需要对其进行隔离、保护。
>
> 至于认证的策略或者处理方式，和Spring security并没有什么区别，而只需要明白，这里它保护的token的资源就可以了。
>
> **当然同时需要开启资源服务器注解**。
>
> 总体来说：Oauth和资源服务器都是基于Spring security来实现的。

OAuth的配置--OAuth2AuthorizationConfig

这次配置在spring boot的启动类中。

1. 开启授权功能（注解）
2. 指定了token存储方式，验证方式
3. 指定客户端的匹配信息
4. token节点的安全策略

```java
@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer // 开启Resource Server服务
public class AuthServiceApplication {
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    /**
     * 这是授权服务器的配置
     */
    @Configuration
    @EnableAuthorizationServer // 开启授权服务功能
    protected class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

        // token存储策略
//        // 存于内存中
//        InMemoryTokenStore inMemoryTokenStore = new InMemoryTokenStore();
//
        // 使用jwt方式
//        @Autowired
//        JwtAccessTokenConverter jwtTokenEnhancer;
//        JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtTokenEnhancer);

        // 存于数据库
        JdbcTokenStore tokenStore = new JdbcTokenStore(dataSource);

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserServiceDetail userServiceDetail;


        /**
         * ClientDetailsServiceConfigurer
         * 配置客户端的一些基本信息
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory() // 放在内存中
                    .withClient("browser") // 创建一个ID为browser的客户端
                    .authorizedGrantTypes("refresh_token", "password") // 配置验证类型为refresh_token和password
//                    .secret("123456") // 密码
                    .scopes("ui") // 客户端域为ui
//                    .authorities() // 授予客户端的权限
                    .and()
                    .withClient("service-hi") // 创建一个ID为service-hi的客户端
                    .secret("123456") // 密码
                    .authorizedGrantTypes("client_credentials", "refresh_token", "password") // 配置授权类型。。。
                    .authorities() // 授予客户端的权限
                    .scopes("server"); // 配置域为
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
            endpoints.tokenStore(tokenStore) // 指定token存储方式
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
            security.tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated")
                    .allowFormAuthenticationForClients() //
                    .passwordEncoder(NoOpPasswordEncoder.getInstance()); // 验证token策略
        }
    }
}
```

token数据库存储的建表sql

```sql
DROP TABLE IF EXISTS `clientdetails`;
CREATE TABLE `clientdetails`
(
    `appId`                  varchar(128) NOT NULL,
    `resourceIds`            varchar(255)  DEFAULT NULL,
    `appSecret`              varchar(255)  DEFAULT NULL,
    `scope`                  varchar(255)  DEFAULT NULL,
    `grantTypes`             varchar(255)  DEFAULT NULL,
    `redirectUrl`            varchar(255)  DEFAULT NULL,
    `authorities`            varchar(255)  DEFAULT NULL,
    `access_token_validity`  int(11) DEFAULT NULL,
    `refresh_token_validity` int(11) DEFAULT NULL,
    `additionalInformation`  varchar(4096) DEFAULT NULL,
    `autoApproveScopes`      varchar(255)  DEFAULT NULL,
    PRIMARY KEY (`appId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`
(
    `token_id`          varchar(256) NOT NULL,
    `token`             blob,
    `authentication_id` varchar(128) NOT NULL,
    `user_name`         varchar(255) DEFAULT NULL,
    `client_id`         varchar(256) DEFAULT NULL,
    `authentication`    blob,
    `refresh_token`     varchar(255) DEFAULT NULL,
    PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals`
(
    `userId`         varchar(256) NOT NULL,
    `clientId`       varchar(256) DEFAULT NULL,
    `scope`          varchar(255) DEFAULT NULL,
    `status`         varchar(10)  DEFAULT NULL,
    `expirestAt`     datetime     DEFAULT NULL,
    `lastModifiedAt` datetime     DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(256) NOT NULL,
    `resource_ids`            varchar(255)  DEFAULT NULL,
    `client_secret`           varchar(255)  DEFAULT NULL,
    `scope`                   varchar(255)  DEFAULT NULL,
    `authorized_grant_types`  varchar(255)  DEFAULT NULL,
    `web_server_redirect_uri` varchar(255)  DEFAULT NULL,
    `authorities`             varchar(255)  DEFAULT NULL,
    `access_token_validity`   int(11) DEFAULT NULL,
    `refresh_token_validity`  int(11) DEFAULT NULL,
    `additional_information`  varchar(4096) DEFAULT NULL,
    `autoapprove`             varchar(255)  DEFAULT NULL,
    PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token`
(
    `token_id`           varchar(256) NOT NULL,
    `token`              blob,
    `authenticatioin_id` varchar(128) NOT NULL,
    `user_name`          varchar(255) DEFAULT NULL,
    `client_id`          varchar(256) DEFAULT NULL,
    PRIMARY KEY (`authenticatioin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
    `code`           varchar(255) DEFAULT NULL,
    `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`
(
    `token_id`       varchar(256) NOT NULL,
    `token`          blob,
    `authentication` blob,
    PRIMARY KEY (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id` bigint(
) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`       bitint(20) NOT NULL AUTO_INCREMENT,
    `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `username` varchar(255) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `user_id` bigint(20) NOT NULL,
    `role_id` bigint(20) NOT NULL,
    KEY       `FKAkdfjlandgu7a987932r` (`role_id`),
    KEY       `FK987asdf9d8gu0ad0f8f` (`user_id`),
    CONSTRAINT `83762tr73t4ty9` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
    CONSTRAINT `8u4t9393hy34u` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```

application.yml

```yaml
spring:
  application:
    name: service-auth
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/spring-cloud-auth?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
server:
  port: 5000
  servlet:
    context-path: /uaa
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

pom.yml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>auth-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>auth-service</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.SR9</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <!--        <dependency>-->
        <!--            <groupId>org.springframework.boot</groupId>-->
        <!--            <artifactId>spring-boot-starter-oauth2-client</artifactId>-->
        <!--        </dependency>-->
        <!--        <dependency>-->
        <!--            <groupId>org.springframework.boot</groupId>-->
        <!--            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>-->
        <!--        </dependency>-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security.oauth.boot</groupId>
            <artifactId>spring-security-oauth2-autoconfigure</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>RELEASE</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

**Resource**

对于同服务器的资源，甚至于可以不配置

对于远程访问授权的还是要做一定的配置，比如指定授权方式是远程授权

> 注意：Resource部分配置代码可能在Client中，这里尽力区分。

这里只对一个接口进行了放行，还是可以做更多的配置

```java
/**
 * <p>
 * 配置Resource Server
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.config
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 18:51
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/user/registry").permitAll() // 这个接口不需要验证
                .anyRequest().authenticated(); // 其它都需要验证
    }

//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.authenticationDetailsSource()
//    }
}
```

application.yml的一部分配置，用来获取

```yaml
security:
  oauth2:
    resource:
      user-info-uri: http://localhost:5000/uaa/users/current
```

**Client**

用以存取获取的token

用以验证并重定向到认证页面

用以定义获取token的uri

配置类

```java
/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.config
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 18:58
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Configuration
@EnableConfigurationProperties
@EnableOAuth2Client // 开启Oauth2 Client的功能
public class Oauth2ClientConfig {

    /**
     * 配置受保护资源的信息
     */
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    /**
     * 配置过滤器,存储当前请求的上下文
     */
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
    }

    /**
     * 在Request域内创建AccessTokenRequest类型的Bean
     */
    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {
        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    }
}
```

配置application.yml 这部分是自定义配置

```yaml
security:
  oauth2:
    client:
      clientId: service-hi
      clientSecret: 123456
      accessTokenUri: http://localhost:5000/uaa/oauth/token
      grantType: client_credentials,password
      scope: server
```

业务辅助类

注意这里包含一个注册功能。并对外无条件暴露。

```java
/**
 * <p>
 * 这是一个测试类
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.controller
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 22:46
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@RestController
@Slf4j
public class HiController {

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String home() {
        return "hi, i am from port: " + port;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/hello")
    public String hello() {
        return "hello you!";
    }

    @GetMapping("/getPrinciple")
    public OAuth2Authentication getPrinciple(OAuth2Authentication oauth2authentication,
                                             Principal principal, Authentication authentication) {
        log.info(oauth2authentication.toString());
        log.info("principal.toString()" + principal.toString());
        log.info("principal.getName()" + principal.getName());
        log.info("authentication:" + authentication.getAuthorities().toString());
        return oauth2authentication;
    }
}

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.controller
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 22:39
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registry", method = RequestMethod.POST)
    public User createUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userService.create(user);
    }
}
/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.service
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 19:42
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
public interface UserService {

    User create(User user);

}
/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.service.impl
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 19:44
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Service
public class UserServiceImpl implements UserService {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private UserDao userDao;

    @Override
    public User create(User user) {
        String hash = ENCODER.encode(user.getPassword());
        user.setPassword(hash);
        User u = userDao.save(user);
        return u;
    }
}
/**
 * <p>
 * 注意：这个类实现了接口UserDetails, Serializable
 * UserDetails这个类是配合Spring Security认证信息的核心接口
 * 这是一个框架接口
 * <p>
 * 其中username可以是名称,也可以是手机号,或邮件
 * 控制权限的可以是角色,也可以是用户其它信息
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description: User实体类
 * @author: chengqj
 * @date: Created in 2021/2/6 15:40
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String password;

}
/**
 * <p>
 * 这个类继承了GrantedAuthority,这个类也是一个框架类
 * <p>
 * getAuthority()可以返回角色名字符串,也可以是别的信息.
 * 这里的权限点是角色名
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/6 16:07
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

}
```

###### 验证步骤

1.首先请求注册用户接口，注册用户

![](D:\chengqj\Documents\typora\image\img_1.png)

2.用用户名和密码，并携带客户端id和secret去token接口获取token

![](D:\chengqj\Documents\typora\image\img_2.png)

> 这里要注意的是，如果不是curl的请求，在Hearder中需要有Authorization：Basic c2VydmljZS1oaToxMjM0NTY=的键值，值是clientId:secret的base64编码
>
> ![](D:\chengqj\Documents\typora\image\img_4.png)

3.然后可以携带token去访问资源了

![](D:\chengqj\Documents\typora\image\img_3.png)

4.请求admin资源

![](D:\chengqj\Documents\typora\image\img_5.png)

5.增加admin角色映射再请求

1）user

![](D:\chengqj\Documents\typora\image\img_6.png) 

2）role

![](D:\chengqj\Documents\typora\image\img_7.png) 

3）user_role

![](D:\chengqj\Documents\typora\image\img_8.png) 

4）请求

![](D:\chengqj\Documents\typora\image\img_9.png)

> 这里有个小插曲：当我在数据库插入user和role关系后，并没有起作用，直到我用用户名和密码重新请求了一次token后，虽然token还是没变，但是却可以请求成功了。应该是重新刷新了库中关系。



#### 使用Spring Cloud OAuth2 和 JWT 保护微服务系统

每次请求都去验证Token的合法性，这在高并发场景下会存在性能瓶颈。JWT可以改善这种情况。

只验证一次获取JWT，然后在资源服务器就可以验证，而不用请求权限服务器。

##### 什么是JWT

JSON Web Token（JWT）是一种开放的标准（RFC7519），JWT定义了一种紧凑且自包含的标准，该标准旨在将各个主体的信息包装为JSON对象。主体信息是通过数字签名进行加密和验证的。常使用HMAC算法或RSA（公钥、私钥的非对称性加密）算法对JWT进行签名，安全性很高。

JWT

- 紧凑性：由于是加密后的字符串，JWT数据体积非常小，可通过POST请求参数或HTTP请求头发送。另外，数据体积小意味着传输速度很快。
- 自包含：JWT包含了主体的所有信息，所以避免了每个请求都需要向Uaa服务验证身份，降低了服务器的负载。

##### JWT结构

JWT由3个部分组成，分别以“.”分隔，组成部分如下。

- Header（头）
- Payload（有效载荷）
- Signnature（签名）

JWT的通常格式如下：

xxxx.yyyyy.zzzzz

**1）Header**

通常由两部分组成：令牌的类型（即JWT）和使用的算法类型，如HMAC、SHA256和RSA。例如

```json
{
	“alg”:"HS256",
	"typ":"JWT"
}
```

将Header用Base64编码作为JWT的第一部分。

**2）Payload**

这部分包含了用户的一些信息和Claim（声明，权利）。有3种类型的Claim：保留，公开和私人。例如

```json
{
	"sub":"12345678",
	"name":"John Doe",
	"admin":true
}
```

将Payload进行Base64编码作为JWT的第二部分。

**3）Signnature**

将创建签名部分，需要将Base64编码后的Header、Payload和密钥进行签名，例如

```
HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
```

**JWT的使用场景如下**

认证：自包含权限信息

信息交换：可以包含信息，并有安全性，所以可以用做信息载体

**JWT流程图**

![](D:\chengqj\Documents\typora\image\img_11.png) 

##### Spring Cloud OAuth2 是通过JWT保护系统的原理

JWT相较于上一篇基于token的原理，唯一不同的就是，在授权服务器和资源服务器增加了JWT的转换器，同时配置了公钥和私钥，用来解密。

##### 代码-授权服务

pom.xml

pom种指定了不处理cert，jks资源文件

> <plugin>
>                 <groupId>org.apache.maven.plugins</groupId>
>                 <artifactId>maven-resources-plugin</artifactId>
>                 <configuration>
>                     <nonFilteredFileExtensions>
>                         <nonFilteredFileExtension>cert</nonFilteredFileExtension>
>                         <nonFilteredFileExtension>jks</nonFilteredFileExtension>
>                     </nonFilteredFileExtensions>
>                 </configuration>
>             </plugin>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.9.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>jwt-auth-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>jwt-auth-service</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.SR10</spring-cloud.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
            <version>1.4.4.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <configuration>
                    <nonFilteredFileExtensions>
                        <nonFilteredFileExtension>cert</nonFilteredFileExtension>
                        <nonFilteredFileExtension>jks</nonFilteredFileExtension>
                    </nonFilteredFileExtensions>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

配置文件application.yml

```yaml
spring:
  application:
    name: jwt-uaa-service
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/spring-cloud-auth?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: root
    password: root
    jpa:
      hibernate:
        ddl-auto: update
      show-sql: true
server:
  port: 9999
  servlet:
    context-path: /uaa
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

java配置类

主要是JwtTokenStore，JwtAccessTokenConverter两个配类，并指定token转化器，指定私钥文件

```java
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

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtTokenEnhancer());
    }
    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer(){
        // 私钥文件
        KeyStoreKeyFactory keyStoreKeyFactory= new KeyStoreKeyFactory(new ClassPathResource("chengqj-jwt.jks"),
                "chengqj123".toCharArray()); // 密码
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("chengqj-jwt")); // keyPair
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

/**
 * <p>
 *
 * </p>
 *
 * @package: com.chengqj.study.jwtauthservice.config
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/17 22:19
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 配置验证管理的Bean
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * HttpSecurity中配置了所有的请求都需要安全验证
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request,response,authenticationException) ->{
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                })
                .and()
                .authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic();
    }
    @Autowired
    private UserServiceDetail userServiceDetail;
    /**
     * 配置了用户信息源和密码加密的策略
     * ps: 密码验证只有配置才会开启
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceDetail).passwordEncoder(new BCryptPasswordEncoder()); // 配置从数据库获取密码
    }
}
```

其它服务类

```java
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
}
/**
 * @Author chengqj
 * @Date 2021/1/1 10:17
 * @Desc
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
/**
 * <p>
 * 这个类继承了GrantedAuthority,这个类也是一个框架类
 * <p>
 * getAuthority()可以返回角色名字符串,也可以是别的信息.
 * 这里的权限点是角色名
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/6 16:07
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @Override
    public String getAuthority() {
        return name;
    }
}
/**
 * <p>
 * 注意：这个类实现了接口UserDetails, Serializable
 * UserDetails这个类是配合Spring Security认证信息的核心接口
 * 这是一个框架接口
 * <p>
 * 其中username可以是名称,也可以是手机号,或邮件
 * 控制权限的可以是角色,也可以是用户其它信息
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description: User实体类
 * @author: chengqj
 * @date: Created in 2021/2/6 15:40
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
@Data
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /**
         * 可以返回角色,也可以返回其它的条件
         */
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        /**
         * 这里可以返回username,也可以返回收集号码或者邮箱地址
         */
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```



##### 代码-资源服务

pom.xml

指定了cert，jks两个资源文件不编译

> <plugin>
>                 <groupId>org.apache.maven.plugins</groupId>
>                 <artifactId>maven-resources-plugin</artifactId>
>                 <configuration>
>                     <nonFilteredFileExtensions>
>                         <nonFilteredFileExtension>cert</nonFilteredFileExtension>
>                         <nonFilteredFileExtension>jks</nonFilteredFileExtension>
>                     </nonFilteredFileExtensions>
>                 </configuration>
>             </plugin>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.9.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chengqj.study</groupId>
    <artifactId>jwt-user-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>jwt-user-service</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.SR10</spring-cloud.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-jwt</artifactId>
            <version>1.0.9.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
            <version>1.4.4.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>2.3.7.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-hystrix</artifactId>
            <version>1.3.6.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>RELEASE</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <configuration>
                    <nonFilteredFileExtensions>
                        <nonFilteredFileExtension>cert</nonFilteredFileExtension>
                        <nonFilteredFileExtension>jks</nonFilteredFileExtension>
                    </nonFilteredFileExtensions>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

配置文件application.yml

```yaml
server:
  port: 9090
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: user-service
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/spring-cloud-auth?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: root
    password: root
    jpa:
      hibernate:
        ddl-auto: update
      show-sql: true
feign:
  hystrix:
    enable: true
```

java配置类

```java
/**
 * <p>
 * 开启权限注解
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.config
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/20 0:06
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GlobalMethodSecurityConfig {
}
/**
 * <p>
 * jwt配置类，同时jwt还可以从远程获取
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.config
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/18 23:13
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Configuration
public class JwtConfig {
//    @Autowired
//    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Bean
    @Qualifier("tokenStore")
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        ClassPathResource classPathResource = new ClassPathResource("public.cert"); // 这里必须要注意，公钥也是可以远程获取的
        String publicKey;
        try {
            publicKey = new String(FileCopyUtils.copyToByteArray(classPathResource.getInputStream()));
        }catch (IOException e){
            publicKey =  getKeyFromAuthorizationServer();
//            throw new RuntimeException(e);
        }
        jwtAccessTokenConverter.setVerifierKey(publicKey);
        jwtAccessTokenConverter.setVerifier(new RsaVerifier(publicKey));
        return jwtAccessTokenConverter;
    }

    /**
     * 通过访问授权服务器获取非对称加密公钥 Key
     *
     * @return 公钥 Key
     */
    private String getKeyFromAuthorizationServer() {
        ObjectMapper objectMapper = new ObjectMapper();
        String pubKey = new RestTemplate().getForObject("http://localhost:9999/uaa/oauth/token_key", String.class);
        try {
            Map map = objectMapper.readValue(pubKey, Map.class);
            return map.get("value").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
/**
 * <p>
 * 配置Resource Server，并指定token转换器
 * </p>
 *
 * @package: com.chengqj.study.authservicehi.config
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/13 18:51
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    @Autowired
    TokenStore tokenStore;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/login","/user/register").permitAll()
                .antMatchers("/**").authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore);
    }
}
```

业务操作类

```java
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
/**
 * @Author chengqj
 * @Date 2021/1/1 10:17
 * @Desc
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
```

Fegin远程调用权限系统

```java
/**
 * <p>
 * feign调用
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
/**
 * <p>
 * hystrix的fallback函数
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
```

Controller

```java
/**
 * <p>
 * 资源类型
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.controller
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/20 15:54
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceDetail userServiceDetail;
	
    // 注册完全暴露，不做权限校验
    @PostMapping("/register")
    public User postUser(@RequestParam("username") String username,@RequestParam("password") String password){
        return userServiceDetail.insertUser(username,password);
    }

    // 远程认证获取JWT
    @PostMapping("/login")
    public UserLoginDTO login(@RequestParam("username")String username,@RequestParam("password") String password){
        return userServiceDetail.login(username,password);
    }

}
**
 * <p>
 * 完全受限资源，需携带JWT访问
 * </p>
 *
 * @package: com.chengqj.study.jwtuserservice.controller
 * @description:
 * @author: chengqj
 * @date: Created in 2021/3/21 18:04
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@RestController
@RequestMapping("/foo")
public class WebController {

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getFoo(){
        return "权限允许：我可以访问这个资源 -- " + UUID.randomUUID().toString();
    }
}
```

实体类

```java
/**
 * <p>
 * 向远程授权获取JWT
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
/**
 * <p>
 * JWT实体类
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
/**
 * <p>
 * 这个类继承了GrantedAuthority,这个类也是一个框架类
 * <p>
 * getAuthority()可以返回角色名字符串,也可以是别的信息.
 * 这里的权限点是角色名
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description:
 * @author: chengqj
 * @date: Created in 2021/2/6 16:07
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Role() {
    }
    
    @Override
    public String getAuthority() {
        return name;
    }

}
/**
 * <p>
 * 注意：这个类实现了接口UserDetails, Serializable
 * UserDetails这个类是配合Spring Security认证信息的核心接口
 * 这是一个框架接口
 * <p>
 * 其中username可以是名称,也可以是手机号,或邮件
 * 控制权限的可以是角色,也可以是用户其它信息
 * </p>
 *
 * @package: com.chengqj.study.security.entity
 * @description: User实体类
 * @author: chengqj
 * @date: Created in 2021/2/6 15:40
 * @copyright: Copyright (c) 2021
 * @version: V1.0
 * @modified:
 */
@Entity
@Data
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /**
         * 可以返回角色,也可以返回其它的条件
         */
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        /**
         * 这里可以返回username,也可以返回收集号码或者邮箱地址
         */
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

异常类

```java
/**
 * <p>
 * 业务异常
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
/**
 * <p>
 * 异常处理器
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
```

工具类

```java
/**
 * <p>
 * 加解密类
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
```

重点

**获取私钥文件**

```shell
keytool -genkeypair -alias chengqj-jwt -validity 3650 -keyalg RSA -dname "CN=jwt,OU=jtw,O=jtw,L=zurich,S=zurich,C=CH" -keypass chengqj123 -keystore chengqj-jwt.jks -storepass chengqj123
```

-alias选项为别名

-keypass 和 -storepass 为密码项

-validity为配置jks文件的过期时间（单位：天）

> 私钥文件需放在授权服务器资源resources路径下

**获取公钥文件**

就着生成的jks文件，执行以下命令

```shell
keytool -list -rfc --keystore chengqj-jwt.jks | openssl x509 -inform pem -pubkey
```

> 注意公钥放在资源服务器的resources下

> **注意**
>
> **公钥文件**：public.cert中的公钥如下
>
> ```
> -----BEGIN PUBLIC KEY-----
> MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhImBQc+IzymrpB0KY3nV
> 4ZwFr4+w2GwjLPU6fIItkGFeFxNyAiLXlIUS0YxCvOYiaaFjwGeIre1gDKyO4wdH
> xOuE08F4BqZyjH7pZj98YfNDch6j/rdUFYKlcl7WeZbtoAAtGvY7keNSfH3D8Jm5
> GCl4Hj7OeXnYnfE4YMuL2ViX6BoukWZAyGb07zXxWjvSNFZoQFbxwI69e2HKEFa2
> 8TD4sr9+/ol/V9d/xAO5iGwYRONbaQ0wNtJj+nZU64J3BHttzhMowaq/je2CBkOR
> sDcM89ZDTZOPS+JsqL/4FTtVgRDEoRWzFMIk98wjHlx7bZf0F03GKDgw9tk6Lakq
> WQIDAQAB
> -----END PUBLIC KEY-----
> ```
>
> 注意首行和末行必须有
>
> **再者**，openssl这个命令生成公钥，但是openssl需要安装一个应用openssl
>
> **还有就是**，在生成公私钥的过程中可能需要输入密码，密码就是私钥中指定的

##### 验证

1.注册用户

![](D:\chengqj\Documents\typora\image\img_12.png)

2.获取token测试

![](D:\chengqj\Documents\typora\image\img_13.png)

3.登录获取JWT

![](D:\chengqj\Documents\typora\image\img_14.png)

4.携带JWT访问受限资源（JWT指定到Authorization中）

![](D:\chengqj\Documents\typora\image\img_10.png)



#### 使用Spring Cloud构建微服务综合案例