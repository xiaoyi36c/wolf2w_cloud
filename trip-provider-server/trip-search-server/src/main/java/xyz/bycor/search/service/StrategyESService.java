package xyz.bycor.search.service;

import xyz.bycor.article.dto.StrategyDTO;
import xyz.bycor.search.domain.StrategyEs;

/**
 * @author BAI
 */
public interface StrategyESService extends SearchService<StrategyDTO> {
    void save(StrategyEs strategyEs);
}