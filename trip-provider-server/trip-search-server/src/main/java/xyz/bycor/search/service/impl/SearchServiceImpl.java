package xyz.bycor.search.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import xyz.bycor.search.domain.BaseEs;
import xyz.bycor.search.qo.SearchQuery;
import xyz.bycor.search.service.SearchService;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author BAI
 */
@Service
public class SearchServiceImpl<T> implements SearchService<T> {

    private final ElasticsearchRestTemplate template;

    public SearchServiceImpl(ElasticsearchRestTemplate template) {
        this.template = template;
    }


    /**
     * 高亮查询
     * @param searchClass 查询的实体类
     * @param qo 查询条件
     * @param handler 查询结果处理器
     * @param fields 高亮字段
     * @return 查询结果
     */
    @Override
    public Page<T> queryWithHighlight(Class<? extends BaseEs> searchClass, SearchQuery qo, Function<Serializable, T> handler, String... fields){
        // 1.构建查询条件对象
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(qo.getKeyword(), fields);
        // 2.设置高亮
        HighlightBuilder field = new HighlightBuilder();
        for (String s : fields) {
            field.field(s);
        }
        // 3.设置高亮前缀
        field.preTags("<em style='color:red'>");
        // 4.设置高亮后缀
        field.postTags("</em>");
        field.requireFieldMatch(false); // 关闭多个高亮显示
        field.numOfFragments(0); // 设置高亮碎片个数
        // 5.设置高亮碎片长度
        field.fragmentSize(100);
        // 构建分页对象
        PageRequest pageRequest = PageRequest.of(qo.getCurrent() - 1, qo.getSize());
        // 6.组合查询条件和分页对象
        NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(multiMatchQueryBuilder).withHighlightBuilder(field).withPageable(pageRequest).build();
        // 7.执行高亮查询
        SearchHits<? extends BaseEs> strategyEs = template.search(build, searchClass);
        // 8.获取高亮结果,封装到实体类中
        List<T> baseEsList = new ArrayList<>();
        for (SearchHit<? extends BaseEs> hit : strategyEs) {
            BaseEs content = hit.getContent();
            // 查询完整的攻略对象
            T dto = handler.apply(content.getId());
            // 获取高亮结果
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            for (Map.Entry<String, List<String>> entry : highlightFields.entrySet()) {
                List<String> value = entry.getValue();
                String highlight = StringUtils.join(value, "");
                String key = entry.getKey();
                try {
                    // 利用内省机制，判断 map 的 key 与对象的属性相同的，自动调用 set 方法设置进去
                    BeanInfo beanInfo = Introspector.getBeanInfo(dto.getClass(), Object.class);
                    // 获取到对象中的所有属性描述对象
                    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                    for (PropertyDescriptor property : propertyDescriptors) {
                        String name = property.getName();
                        Method setter = property.getWriteMethod();
                        if (key.equals(name) && setter != null) {
                            setter.invoke(dto, highlight);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            baseEsList.add(dto);
        }
        return new PageImpl<>(baseEsList, pageRequest, strategyEs.getTotalHits());
    }


}
