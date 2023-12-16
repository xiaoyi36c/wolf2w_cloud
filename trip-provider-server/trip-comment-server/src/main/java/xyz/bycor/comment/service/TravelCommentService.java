package xyz.bycor.comment.service;

import xyz.bycor.auth.vo.LoginResponseUser;
import xyz.bycor.comment.domian.TravelComment;
import xyz.bycor.comment.qo.CommentQuery;

import java.util.List;

public interface TravelCommentService {

    void save(TravelComment TravelComment, LoginResponseUser user);



    List<TravelComment> findList(CommentQuery qo);

}
