package xyz.bycor.comment.service.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import xyz.bycor.auth.vo.LoginResponseUser;
import xyz.bycor.comment.domian.TravelComment;
import xyz.bycor.comment.feign.UserInfoFeignService;
import xyz.bycor.comment.qo.CommentQuery;
import xyz.bycor.comment.repository.TravelCommentRepository;
import xyz.bycor.comment.service.TravelCommentService;
import xyz.bycor.core.exception.BusinessException;
import xyz.bycor.framework.redis.config.RedisJsonService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TravelCommentServiceImpl implements TravelCommentService {

    private final TravelCommentRepository commentRepository;
    private final UserInfoFeignService userInfoFeignService;

    private final MongoTemplate mongoTemplate;


    private final RedisJsonService redisJsonService;

    public TravelCommentServiceImpl(TravelCommentRepository TravelCommentRepository, UserInfoFeignService userInfoFeignService, MongoTemplate mongoTemplate, RedisJsonService redisJsonService) {
        this.commentRepository = TravelCommentRepository;
        this.userInfoFeignService = userInfoFeignService;
        this.mongoTemplate = mongoTemplate;
        this.redisJsonService = redisJsonService;
    }


    @Override
    public void save(TravelComment comment, LoginResponseUser user) {

        // 2. 设置用户信息
        comment.setUserId(user.getId());
        comment.setNickname(user.getNickname());
        comment.setCity(user.getCity());
        comment.setLevel(user.getLevel());
        comment.setHeadImgUrl(user.getHeadImgUrl());
        comment.setCreateTime(new Date());
        // 3.关联评论
        if (comment.getType() ==TravelComment.TRAVEL_COMMENT_TYPE && comment.getRefComment() !=null){
            Optional<TravelComment> optional = commentRepository.findById(comment.getRefComment().getId());
            TravelComment refComment = optional.orElseThrow(() -> new BusinessException(400, "评论的数据不存在"));
            refComment.setRefComment(null);
            comment.setRefComment(refComment);
        }
        // 4. 保存评论
        commentRepository.save(comment);
    }

    @Override
    public List<TravelComment> findList(CommentQuery qo) {
        return commentRepository.findByTravelId(qo.getArticleId());
    }


}
