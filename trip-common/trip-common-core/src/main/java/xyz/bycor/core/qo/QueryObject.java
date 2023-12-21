package xyz.bycor.core.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QueryObject {
    private Integer current = 1;
    private Integer size = 10;
    private String keyword;

    public Integer getStart() {
        return (current - 1) * size;
    }
}
