package xyz.bycor.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *  搜索服务启动类
 * @author BAI
 */
@SpringBootApplication
@EnableFeignClients
public class TripSearchServerApp{
    public static void main(String[] args) {
        SpringApplication.run(TripSearchServerApp.class, args);
    }
}