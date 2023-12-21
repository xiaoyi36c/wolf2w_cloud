package xyz.bycor.search.service.impl;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import xyz.bycor.article.dto.TravelDTO;
import xyz.bycor.search.service.TravelESService;

/**
 * @author BAI
 */
@Service
public class TravelEsServiceImpl extends SearchServiceImpl<TravelDTO> implements TravelESService {
    public TravelEsServiceImpl(ElasticsearchRestTemplate template) {
        super(template);
    }
}
