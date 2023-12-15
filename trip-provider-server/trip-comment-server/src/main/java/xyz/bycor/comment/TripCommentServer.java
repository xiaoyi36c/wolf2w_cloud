package xyz.bycor.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients //开启feign
public class TripCommentServer {
    public static void main(String[] args) {
        SpringApplication.run(TripCommentServer.class, args);
    }

}
