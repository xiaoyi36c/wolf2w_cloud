package xyz.bycor.data.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.bycor.article.domain.Strategy;
import xyz.bycor.article.qo.StatDataQuery;
import xyz.bycor.article.vo.StatData;

import java.util.List;

@FeignClient("trip-article-server")
public interface ArticleFeignService {

    @GetMapping("/strategies/stat/lastSevenDay")
    List<StatData> findStatByLastSevenDay(@SpringQueryMap StatDataQuery statQuery);


    @PostMapping("/strategies/stat/saveBatch")
    void saveBatch(@RequestBody List<Strategy> list);
}
