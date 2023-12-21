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
    public static final int  TYPE_DEST=0;
    public static final int  TYPE_STRATEGY =1;
    public static final int  TYPE_TRAVEL  =2;
    public static final int  TYPE_USER   =3;

    private Integer type;
}
