package xyz.bycor.search.service;

import org.springframework.data.domain.Page;
import xyz.bycor.search.domain.BaseEs;
import xyz.bycor.search.qo.SearchQuery;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author BAI
 */
public interface SearchService<T>  {
    Page<T> queryWithHighlight(Class<? extends BaseEs> searchClass, SearchQuery qo, Function<Serializable, T> handler, String... fields);

}
