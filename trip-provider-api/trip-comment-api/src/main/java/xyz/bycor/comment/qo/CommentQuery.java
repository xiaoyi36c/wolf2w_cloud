package xyz.bycor.comment.qo;

import lombok.Getter;
import lombok.Setter;
import xyz.bycor.core.qo.QueryObject;

@Getter
@Setter
public class CommentQuery extends QueryObject {
        private Long articleId;
}
