package xyz.bycor.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients //开启feign
@EnableScheduling //开启定时任务
public class TripDataServer {
    public static void main(String[] args) {
        SpringApplication.run(TripDataServer.class, args);
    }

}
