package xyz.bycor.search.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.search.domain.StrategyEs;
import xyz.bycor.search.service.impl.StrategyEsServiceImpl;

@RestController
@RequestMapping("/strategies")
@Slf4j
@Api(tags = "攻略搜索接口")
public class StrategyController {
    private final StrategyEsServiceImpl strategyEsService;

    public StrategyController(StrategyEsServiceImpl strategyEsService) {
        this.strategyEsService = strategyEsService;
    }

    @PostMapping("/save")
    @ApiOperation("新增搜索对象")
    public void createStrategy(StrategyEs strategyEs) {
        log.info("新增搜索对象：{}", strategyEs);
        strategyEsService.save(strategyEs);
    }


    @GetMapping("/highlight")
    @ApiOperation("高亮搜索")
    public Page<StrategyEs> highlight(String keyword, Integer current, Integer size) {
        log.info("搜索关键字：{}", keyword);
        return strategyEsService.queryWithHighlight(keyword,current,size);
    }
}
