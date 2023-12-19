package xyz.bycor.search.service;

import xyz.bycor.search.domain.StrategyEs;

import java.util.List;

public interface IStrategyEsService {
    void save(StrategyEs strategyEs);
    void update(StrategyEs strategyEs);
    void delete(String id);
    StrategyEs get(String id);
    List<StrategyEs> list();

    List<StrategyEs> queryWithHighlight(String keyword, Integer current, Integer size);
}