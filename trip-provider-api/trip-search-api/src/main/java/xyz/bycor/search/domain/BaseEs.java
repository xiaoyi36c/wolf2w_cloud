package xyz.bycor.search.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @author BAI
 */

@Getter
@Setter
public class BaseEs implements Serializable {

    @Id
    @Field(type = FieldType.Long)
    private Long id;  //主键id
}
