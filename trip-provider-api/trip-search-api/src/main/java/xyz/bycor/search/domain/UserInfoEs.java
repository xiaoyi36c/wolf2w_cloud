package xyz.bycor.search.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 用户搜索对象
 */
@Getter
@Setter
@Document(indexName=UserInfoEs.INDEX_NAME)
public class UserInfoEs extends BaseEs {
    public static final String INDEX_NAME = "users";
    @Field(type = FieldType.Keyword)
    private String city;
    @Field(analyzer="ik_max_word",searchAnalyzer="ik_max_word",type = FieldType.Text)
    private String info;
}