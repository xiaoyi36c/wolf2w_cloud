package xyz.bycor.comment.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.bycor.comment.domian.TravelComment;

import java.util.List;


public interface TravelCommentRepository extends MongoRepository<TravelComment,String> {
    List<TravelComment> findByTravelId(Long articleId);
}
