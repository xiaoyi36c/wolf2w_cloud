package xyz.bycor.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TripArticleServer {
    public static void main(String[] args) {
        SpringApplication.run(TripArticleServer.class, args);
    }

}
