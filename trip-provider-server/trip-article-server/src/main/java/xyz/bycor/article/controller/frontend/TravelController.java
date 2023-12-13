package xyz.bycor.article.controller.frontend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.article.domain.Strategy;
import xyz.bycor.article.domain.Travel;
import xyz.bycor.article.qo.TravelQuery;
import xyz.bycor.article.service.TravelService;
import xyz.bycor.core.response.R;

import java.util.List;

@Api(tags = "游记详细接口")
@RestController
@RequestMapping("/travels")
@Slf4j
public class TravelController {
    private final TravelService travelService;


    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    /**
     * 目的地下的游记内容 分页条件查询
     */
    @GetMapping()
    public R<Page<Travel>> findTravelByDestId(TravelQuery travelQuery) {
        log.info("目的地下的游记内容 分页条件查询");
        return R.ok(travelService.findTravelByDestId(travelQuery));
    }


    @ApiOperation(value = "游记Top3")
    @GetMapping("/viewnumTop3")
    public R<List<Travel>> viewnumTop3(Long destId) {
        log.info("游记Top3");
        return R.ok(travelService.findTravelTop3(destId));
    }
}