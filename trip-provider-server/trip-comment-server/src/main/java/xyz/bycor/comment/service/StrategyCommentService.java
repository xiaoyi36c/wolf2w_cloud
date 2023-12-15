package xyz.bycor.comment.service;

import xyz.bycor.comment.domian.StrategyComment;

import java.util.List;

public interface StrategyCommentService {

    void save(StrategyComment strategyComment);


    void delete(Long id);

    void update(StrategyComment strategyComment);

    StrategyComment findById(Long id);

    List<StrategyComment> findList();

}
