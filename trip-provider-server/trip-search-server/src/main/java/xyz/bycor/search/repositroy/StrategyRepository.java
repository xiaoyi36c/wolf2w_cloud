package xyz.bycor.search.repositroy;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import xyz.bycor.search.domain.StrategyEs;

public interface StrategyRepository extends ElasticsearchRepository<StrategyEs, Long> {
}
