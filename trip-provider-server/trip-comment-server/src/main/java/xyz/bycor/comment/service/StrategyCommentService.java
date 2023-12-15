package xyz.bycor.comment.service;

import org.springframework.data.domain.Page;
import xyz.bycor.comment.domian.StrategyComment;
import xyz.bycor.comment.qo.StrategyCommentQuery;

import java.util.List;

public interface StrategyCommentService {

    void save(StrategyComment strategyComment);


    void delete(String id);

    void update(StrategyComment strategyComment);

    StrategyComment findById(String id);

    List<StrategyComment> findList();

    Page<StrategyComment> findPage(StrategyCommentQuery qo);

    void likes(String id);
}
