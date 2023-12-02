package xyz.bycor.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * trip-provider-server 用户服务
 *
 * @auther BaiShuai
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
public class TripMemberServer {

    public static void main(String[] args) {
        SpringApplication.run(TripMemberServer.class, args);
    }
}
