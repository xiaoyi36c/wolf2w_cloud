package xyz.bycor.data.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.bycor.article.dto.StrategyDTO;
import xyz.bycor.data.feign.ArticleFeignService;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  策略数据持久化任务
 *  执行时间: 每天凌晨1点
 */

@Component
@Slf4j
public class StrategyDataPersistenceJob {

    private final RedisJsonService redisJsonService;

    private final ArticleFeignService articleFeignService;

    public StrategyDataPersistenceJob(RedisJsonService redisJsonService, ArticleFeignService articleFeignService) {
        this.redisJsonService = redisJsonService;
        this.articleFeignService = articleFeignService;
    }


    //@Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0 0/30 * * * ?")
    public void execute() {
        log.info("策略数据持久化任务开始执行");
        // 1. 获取前一天更新的策略数据
        // 转成时间戳
        long max = toTimestamp(LocalDateTime.now());
        long min = toTimestamp(LocalDateTime.now().minusDays(1));
        // 2. 获取策略数据
        Set<Object> ids = redisJsonService.reverseRangeByScore(RedisEnum.STRATEGY_STAT_DATA_ZSET, min, max);
        if (ids.isEmpty()) {
            return;
        }
        List<Integer> idList = ids.stream().map(id -> (Integer) id).collect(Collectors.toList());

        // 3.分批处理,每批1000条
        int page = ids.size() % 1000 == 0 ? ids.size() / 1000 : ids.size() / 1000 + 1;
        for (int i = 0; i < page; i++) {
            log.info("策略数据持久化任务开始执行,第{}批", i);
            List<StrategyDTO> list =new ArrayList<>(1000);
            // 3.1 针对每批数据进行处理,每次处理1000条
            for (int j = i * 1000; j < Math.min(j + 1000, ids.size()); j++) {
                // 3.2 获取策略数据
                Map<Object, Object> map = redisJsonService.hgetAll(RedisEnum.STRATEGY_STAT_DATA_HASH, idList.get(j) + "");
                list.add(mapToStrategy(map));
            }
            // 4. 持久化策略数据
            articleFeignService.saveBatch(list);
            log.info("策略数据持久化任务开始执行,第{}批,处理完成,数据为{}", i, list);
        }

    }

    private StrategyDTO mapToStrategy(Map<Object, Object> map) {
        StrategyDTO strategy = new StrategyDTO();
        strategy.setId(Long.valueOf((Integer)map.get("id")));
        strategy.setViewnum((Integer)map.get("viewnum"));
        strategy.setReplynum((Integer)map.get("replynum"));
        strategy.setFavornum((Integer)map.get("favornum"));
        strategy.setSharenum((Integer)map.get("sharenum"));
        strategy.setThumbsupnum((Integer)map.get("thumbsupnum"));
        strategy.setLastUpdateTime(new Date());
        return strategy;
    }

    private Long toTimestamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }


}
