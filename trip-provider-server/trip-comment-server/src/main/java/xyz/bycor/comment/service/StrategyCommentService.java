package xyz.bycor.comment.service;

import org.springframework.data.domain.Page;
import xyz.bycor.auth.vo.LoginResponseUser;
import xyz.bycor.comment.domian.StrategyComment;
import xyz.bycor.comment.qo.CommentQuery;

import java.util.List;

public interface StrategyCommentService {

    void save(StrategyComment strategyComment, LoginResponseUser user);


    void delete(String id);

    void update(StrategyComment strategyComment);

    StrategyComment findById(String id);

    List<StrategyComment> findList();

    Page<StrategyComment> findPage(CommentQuery qo);

    void likes(String id, LoginResponseUser user);
}
