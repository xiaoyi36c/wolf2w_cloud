package xyz.bycor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * gateway service 网关服务
 */

@SpringBootApplication
@EnableDiscoveryClient
public class TripGatewayService {
    public static void main(String[] args) {
        SpringApplication.run(TripGatewayService.class, args);
    }
}
