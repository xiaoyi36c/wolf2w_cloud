package xyz.bycor.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.bycor.auth.listener.AuthApplicationStartedListener;
import xyz.bycor.framework.redis.config.RedisJsonService;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthApplicationConfig {
    @Bean
    public AuthApplicationStartedListener authApplicationStartedListener(ApplicationContext ctx, RedisJsonService redisJsonService) {
        return new AuthApplicationStartedListener(ctx, redisJsonService);
    }

}
