package xyz.bycor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * gateway service 网关服务
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableOpenApi
public class TripGatewayService {
    public static void main(String[] args) {
        SpringApplication.run(TripGatewayService.class, args);
    }
}
