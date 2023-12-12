package xyz.bycor.article.qo;

import lombok.Getter;
import lombok.Setter;
import xyz.bycor.core.qo.QueryObject;
@Getter
@Setter
public class StrategyQuery extends QueryObject {
    public static final int TYPE_THEME =3;

    private String orderBy;

    private Long refId;
    private Integer type;

    private Long destId;
    private Long themeId;
}
