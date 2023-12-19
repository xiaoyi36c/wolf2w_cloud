package xyz.bycor.search.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xyz.bycor.core.qo.QueryObject;

/**
 * @author BAI
 */
@Getter
@Setter
@ToString
public class SearchQuery extends QueryObject {
    private Integer type;
}
