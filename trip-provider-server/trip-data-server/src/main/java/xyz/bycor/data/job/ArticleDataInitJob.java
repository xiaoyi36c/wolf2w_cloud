package xyz.bycor.data.job;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.bycor.article.qo.StatDataQuery;
import xyz.bycor.article.vo.StatData;
import xyz.bycor.data.feign.ArticleFeignService;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;

import java.time.LocalDate;
import java.util.List;

@Component
public class ArticleDataInitJob {
    private final RedisJsonService redisJsonService;
    private final ArticleFeignService articleFeignService;

    public ArticleDataInitJob(RedisJsonService redisJsonService, ArticleFeignService articleFeignService) {
        this.redisJsonService = redisJsonService;
        this.articleFeignService = articleFeignService;
    }


    @Scheduled(cron = "0/10 * * * * ?") //0 0 5 1/7 * ?
    public void initArticleData() {
        // 定义一个时间变量，用于存储当前时间前7天时间
        StatDataQuery statQuery = new StatDataQuery();
        statQuery.setDate(LocalDate.now().minusDays(7));
        statQuery.setCurrent(1);
        statQuery.setSize(3);
        do {
            //  批量查询7天内的文章数据
            List<StatData> statData = articleFeignService.findStatByLastSevenDay(statQuery);
            if (CollectionUtils.isEmpty(statData)) {
                break;
            }
            // 循环判断文章是否存在，如果不存在则插入
            for (StatData data : statData) {
                // 根据文章id查询文章是否存在
                boolean exists = redisJsonService.exists(RedisEnum.ARTICLE_STAT_DATA_HASH, data.getId()+"");
                // 如果不存在则插入
                if (!exists) {
                    JSONObject map = (JSONObject) JSON.toJSON(data);
                    redisJsonService.putAll(RedisEnum.ARTICLE_STAT_DATA_HASH, map,data.getId()+"");
                }
            }
            // 查询下一页
            statQuery.setCurrent(statQuery.getCurrent() + 1);
        } while (true);


    }

}
