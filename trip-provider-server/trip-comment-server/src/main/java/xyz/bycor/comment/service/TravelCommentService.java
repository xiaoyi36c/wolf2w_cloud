package xyz.bycor.comment.service;

import xyz.bycor.comment.domian.TravelComment;
import xyz.bycor.comment.qo.CommentQuery;

import java.util.List;

public interface TravelCommentService {

    void save(TravelComment TravelComment);


    void delete(String id);

    void update(TravelComment TravelComment);

    TravelComment findById(String id);

    List<TravelComment> findList(CommentQuery qo);

}
