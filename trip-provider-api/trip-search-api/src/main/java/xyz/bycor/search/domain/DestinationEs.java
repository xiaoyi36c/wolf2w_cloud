package xyz.bycor.search.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 目的地搜索对象
 * @author BAI
 */
@Getter
@Setter
@Document(indexName=DestinationEs.INDEX_NAME)
public class DestinationEs extends BaseEs {
    public static final String INDEX_NAME = "destinations";

    @Field(type = FieldType.Keyword)
    private String name;
    @Field(analyzer="ik_max_word",searchAnalyzer="ik_max_word",type = FieldType.Text)
    private String info;
}