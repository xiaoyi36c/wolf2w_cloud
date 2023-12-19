package xyz.bycor.search.service.impl;

import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import xyz.bycor.search.domain.StrategyEs;
import xyz.bycor.search.repositroy.StrategyRepository;
import xyz.bycor.search.service.IStrategyEsService;

import java.util.List;

@Service
public class StrategyEsServiceImpl implements IStrategyEsService {

    private final StrategyRepository strategyRepository;

    private final ElasticsearchRestTemplate template;

    public StrategyEsServiceImpl(StrategyRepository strategyRepository, ElasticsearchRestTemplate template) {
        this.strategyRepository = strategyRepository;
        this.template = template;
    }

    @Override
    public void save(StrategyEs strategyEs) {
        strategyRepository.save(strategyEs);
    }

    @Override
    public void update(StrategyEs strategyEs) {
        strategyRepository.save(strategyEs);
    }

    @Override
    public void delete(String id) {
        strategyRepository.deleteById(Long.parseLong(id));

    }

    @Override
    public StrategyEs get(String id) {
        return strategyRepository.findById(Long.parseLong(id)).get();
    }

    @Override
    public List<StrategyEs> list() {
        return (List<StrategyEs>) strategyRepository.findAll();
    }

    @Override
    public List<StrategyEs> queryWithHighlight(String keyword, Integer current, Integer size) {
        // 1.构建查询条件对象
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "subTitle", "summary");
        // 2.设置高亮
        HighlightBuilder field = new HighlightBuilder().field("title").field("subTitle").field("summary");
        // 3.设置高亮前缀
        field.preTags("<em style='color:red'>");
        // 4.设置高亮后缀
        field.postTags("</em>");
        field.requireFieldMatch(false); // 关闭多个高亮显示
        field.numOfFragments(0); // 设置高亮碎片个数
        // 5.设置高亮碎片长度
        field.fragmentSize(100);
        // 构建分页对象
        PageRequest pageRequest = PageRequest.of(current - 1, size);
        // 6.组合查询条件和分页对象
        NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(multiMatchQueryBuilder).withHighlightBuilder(field).withPageable(pageRequest).build();
        // 7.执行高亮查询
        SearchHits<StrategyEs> strategyEs = template.search(build, StrategyEs.class);
        // 8.获取高亮结果,封装


        return null;
    }
}
