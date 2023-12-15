package xyz.bycor.comment.qo;

import lombok.Getter;
import lombok.Setter;
import xyz.bycor.core.qo.QueryObject;

@Getter
@Setter
public class StrategyCommentQuery extends QueryObject {
        private Long strategyId;
        // 跳过多少条数据
        private Integer start;



}
