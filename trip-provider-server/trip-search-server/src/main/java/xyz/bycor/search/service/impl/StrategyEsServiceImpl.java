package xyz.bycor.search.service.impl;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import xyz.bycor.article.dto.StrategyDTO;
import xyz.bycor.search.domain.StrategyEs;
import xyz.bycor.search.feign.ArticleFeignService;
import xyz.bycor.search.repositroy.StrategyRepository;
import xyz.bycor.search.service.IStrategyEsService;

/**
 * @author BAI
 */
@Service
public class StrategyEsServiceImpl extends SearchServiceImpl<StrategyDTO> implements IStrategyEsService {

    private final StrategyRepository strategyRepository;


    public StrategyEsServiceImpl(StrategyRepository strategyRepository, ElasticsearchRestTemplate template, ArticleFeignService articleFeignService) {
        super( template);
        this.strategyRepository = strategyRepository;
    }

    @Override
    public void save(StrategyEs strategyEs) {
        strategyRepository.save(strategyEs);
    }



}
