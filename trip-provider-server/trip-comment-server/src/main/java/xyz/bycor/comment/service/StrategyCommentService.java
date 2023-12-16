package xyz.bycor.comment.service;

import org.springframework.data.domain.Page;
import xyz.bycor.auth.vo.LoginResponseUser;
import xyz.bycor.comment.domian.StrategyComment;
import xyz.bycor.comment.qo.CommentQuery;

public interface StrategyCommentService {

    void save(StrategyComment strategyComment, LoginResponseUser user);



    Page<StrategyComment> findPage(CommentQuery qo);

    void likes(String id, LoginResponseUser user);
}
