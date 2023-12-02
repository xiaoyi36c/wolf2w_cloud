package xyz.bycor.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * trip-provider-server 用户服务
 *
 * @auther BaiShuai
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TripProviderServer {

    public static void main(String[] args) {
        SpringApplication.run(TripProviderServer.class, args);
    }
}
