package xyz.bycor.article.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import xyz.bycor.core.event.StatDataEvent;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;

@Component
@Slf4j
public class StatEventListener implements ApplicationListener<StatDataEvent> {

    private final RedisJsonService redisJsonService;

    public StatEventListener(RedisJsonService redisJsonService) {
        this.redisJsonService = redisJsonService;
    }

    @Override
    public void onApplicationEvent(StatDataEvent statDataEvent) {
        // 操作数据库,实现数据的统计
        StatDataEvent.Data source = (StatDataEvent.Data) statDataEvent.getSource();
        log.info("监听到的数据：{}",source);

        redisJsonService.increment(RedisEnum.ARTICLE_STAT_DATA_HASH,source.getField(),source.getValue(),source.getId().toString());


    }
}
