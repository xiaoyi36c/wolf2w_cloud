package xyz.bycor.search.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 游记搜索对象
 */
@Getter
@Setter
@Document(indexName=TravelEs.INDEX_NAME)
public class TravelEs extends BaseEs {
    public static final String INDEX_NAME = "travels";
    @Field(index=true,analyzer="ik_max_word",searchAnalyzer="ik_max_word",type = FieldType.Text)
    private String title;  //游记标题
    @Field(index=true,analyzer="ik_max_word",searchAnalyzer="ik_max_word",type = FieldType.Text)
    private String summary; //游记简介
}