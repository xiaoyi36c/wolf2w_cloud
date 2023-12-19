package xyz.bycor.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.search.domain.StrategyEs;
import xyz.bycor.search.service.impl.StrategyEsServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/strategies")
public class StrategyController {
    @Autowired
    private StrategyEsServiceImpl strategyEsService;

    @PostMapping("/save")
    public void createStrategy(StrategyEs strategyEs) {
        strategyEsService.save(strategyEs);
    }


    @GetMapping("/highlight")
    public List<StrategyEs> highlight(String keyword, Integer current, Integer size) {
        return strategyEsService.queryWithHighlight(keyword,current,size);
    }
}
