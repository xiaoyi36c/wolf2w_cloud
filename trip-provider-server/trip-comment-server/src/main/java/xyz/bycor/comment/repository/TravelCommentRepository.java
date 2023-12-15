package xyz.bycor.comment.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.bycor.comment.domian.TravelComment;


public interface TravelCommentRepository extends MongoRepository<TravelComment,String> {
}
