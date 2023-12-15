package xyz.bycor.comment.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.bycor.comment.domian.StrategyComment;



public interface StrategyCommentRepository extends MongoRepository<StrategyComment,String> {
}
