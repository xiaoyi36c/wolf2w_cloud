package xyz.bycor.search.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 攻略搜索对象
 * @author BAI
 */
@Getter
@Setter
@ToString
@Document(indexName=StrategyEs.INDEX_NAME)
public class StrategyEs extends BaseEs {

    public static final String INDEX_NAME = "strategies";

    @Field(analyzer="ik_max_word",searchAnalyzer="ik_max_word",type = FieldType.Text)
    private String title;  //攻略标题
    @Field(analyzer="ik_max_word",searchAnalyzer="ik_max_word",type = FieldType.Text)
    private String subTitle;  //攻略标题
    @Field(analyzer="ik_max_word",searchAnalyzer="ik_max_word",type = FieldType.Text)
    private String summary; //攻略简介
}