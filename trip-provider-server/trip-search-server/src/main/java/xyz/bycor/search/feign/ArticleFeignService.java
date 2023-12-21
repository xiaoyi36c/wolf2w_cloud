package xyz.bycor.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.bycor.article.dto.DestinationDTO;
import xyz.bycor.article.dto.StrategyDTO;
import xyz.bycor.article.dto.TravelDTO;

import java.io.Serializable;
import java.util.List;

/**
 *  远程调用文章服务的接口
 * @author BAI
 */
@FeignClient("trip-article-server")
public interface ArticleFeignService {

    @GetMapping("/destinations/getDestByName")
    DestinationDTO getDestByName(@RequestParam String destName) ;


    @GetMapping("/strategies/findStrategiesByDestId")
    List<StrategyDTO> findStrategiesByDestId(@RequestParam Long destId);

    @GetMapping("/travels/findTravelsByDestId")
    List<TravelDTO> findTravelsByDestId(@RequestParam Long destId);

    @GetMapping("/strategies/getStrategyById")
    StrategyDTO getStrategyById(@RequestParam Serializable id);
}
