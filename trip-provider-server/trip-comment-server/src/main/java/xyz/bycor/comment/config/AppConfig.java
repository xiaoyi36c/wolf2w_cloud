package xyz.bycor.comment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.bycor.core.utils.SpringContextUtil;

@Configuration
public class AppConfig {

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
}
