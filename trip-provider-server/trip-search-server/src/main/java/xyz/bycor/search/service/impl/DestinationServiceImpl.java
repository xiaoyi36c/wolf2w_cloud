package xyz.bycor.search.service.impl;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import xyz.bycor.article.dto.DestinationDTO;
import xyz.bycor.search.service.DestinationESService;

/**
 * @author BAI
 */
@Service
public class DestinationServiceImpl  extends  SearchServiceImpl<DestinationDTO>  implements DestinationESService {
    public DestinationServiceImpl(ElasticsearchRestTemplate template) {
        super(template);
    }
}
