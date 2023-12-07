package xyz.bycor.core.qo;

import lombok.Data;

@Data
public class QueryObject {
    private Integer current = 1;
    private Integer size = 10;
    private String keyword;
}
