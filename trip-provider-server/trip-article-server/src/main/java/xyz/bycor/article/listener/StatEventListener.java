package xyz.bycor.article.listener;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import xyz.bycor.article.domain.Strategy;
import xyz.bycor.article.service.StrategyService;
import xyz.bycor.core.event.StatDataEvent;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;

@Component
@Slf4j
public class StatEventListener implements ApplicationListener<StatDataEvent> {

    private final RedisJsonService redisJsonService;

    private final StrategyService strategyService;

    public StatEventListener(RedisJsonService redisJsonService, StrategyService strategyService) {
        this.redisJsonService = redisJsonService;
        this.strategyService = strategyService;
    }

    @Override
    public void onApplicationEvent(StatDataEvent statDataEvent) {
        // 操作数据库,实现数据的统计
        StatDataEvent.Data source = (StatDataEvent.Data) statDataEvent.getSource();
        log.info("监听到的数据：{}",source);
        // 根据文章id查询文章是否存在
        if (!redisJsonService.exists(RedisEnum.ARTICLE_STAT_DATA_HASH,source.getId().toString())) {
            // 如果不存则查询数据库
            Strategy strategy = strategyService.getById(source.getId());
            JSONObject strategyMap = new JSONObject();
            strategyMap.put("id",strategy.getId());
            strategyMap.put("viewnum",strategy.getViewnum());
            strategyMap.put("replynum",strategy.getReplynum());
            strategyMap.put("favornum",strategy.getFavornum());
            strategyMap.put("sharenum",strategy.getSharenum());
            strategyMap.put("thumbsupnum",strategy.getThumbsupnum());
            redisJsonService.putAll(RedisEnum.ARTICLE_STAT_DATA_HASH,strategyMap,strategy.getId().toString());
        }
        redisJsonService.increment(RedisEnum.ARTICLE_STAT_DATA_HASH,source.getField(),source.getValue(),source.getId().toString());
    }
}
