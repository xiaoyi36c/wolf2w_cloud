package xyz.bycor.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import xyz.bycor.filter.CheckLoginTokenFilter;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;

import java.util.Set;

@Slf4j
@Component
public class AuthBlackListMessageListener implements MessageListener{


    private final RedisJsonService redisJsonService;

    private CheckLoginTokenFilter jwtTokenFilter;

    public AuthBlackListMessageListener(RedisJsonService redisJsonService,CheckLoginTokenFilter jwtTokenFilter) {
        this.redisJsonService = redisJsonService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = redisJsonService.stringDeserializer(message.getChannel());
        Object value = redisJsonService.valueDeserializer(message.getBody());

        log.info("[黑名单订阅频道] 收到黑名单接口更新消息: channel={}, message={}", channel, value);
        Set<Object> newBlackListUrls = redisJsonService.members(RedisEnum.AUTH_BLACKLIST);
        jwtTokenFilter.setBlackListUrls(newBlackListUrls);
    }
}
