package xyz.bycor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import xyz.bycor.framework.redis.config.key.RedisEnum;
import xyz.bycor.listener.AuthBlackListMessageListener;

@Configuration
public class GatewayRedisConfig {

    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory,
            AuthBlackListMessageListener authBlackListMessageListener
    ) {
        // 创建一个 Redis 发布订阅消息容器对象, 这个对象用来管理订阅消息的监听器
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 关联到 redis 的连接工厂
        container.setConnectionFactory(connectionFactory);
        // 配置容器对象关联指定的监听器到对应的 channel 上
        container.addMessageListener(authBlackListMessageListener, new ChannelTopic(RedisEnum.AUTH_BLACKLIST_CHANNEL.join()));
        return container;
    }
}