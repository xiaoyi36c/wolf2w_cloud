package xyz.bycor.article.qo;

import lombok.Getter;
import lombok.Setter;
import xyz.bycor.core.qo.QueryObject;
@Getter
@Setter
public class StrategyQuery extends QueryObject {
    private Long destId;
    private Long themeId;
}
