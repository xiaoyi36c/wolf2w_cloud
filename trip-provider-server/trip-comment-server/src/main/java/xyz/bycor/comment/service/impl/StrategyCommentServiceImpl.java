package xyz.bycor.comment.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import xyz.bycor.comment.domian.StrategyComment;
import xyz.bycor.comment.feign.UserInfoFeignService;
import xyz.bycor.comment.qo.CommentQuery;
import xyz.bycor.comment.repository.StrategyCommentRepository;
import xyz.bycor.comment.service.StrategyCommentService;
import xyz.bycor.core.exception.BusinessException;
import xyz.bycor.core.utils.JwtUtils;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;
import xyz.bycor.member.dto.UserinfoDTO;
import xyz.bycor.member.vo.LoginResponseUser;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StrategyCommentServiceImpl implements StrategyCommentService {

    private final StrategyCommentRepository commentRepository;
    private final UserInfoFeignService userInfoFeignService;

    private final MongoTemplate mongoTemplate;


    private final RedisJsonService redisJsonService;

    public StrategyCommentServiceImpl(StrategyCommentRepository strategyCommentRepository, UserInfoFeignService userInfoFeignService, MongoTemplate mongoTemplate, RedisJsonService redisJsonService) {
        this.commentRepository = strategyCommentRepository;
        this.userInfoFeignService = userInfoFeignService;
        this.mongoTemplate = mongoTemplate;
        this.redisJsonService = redisJsonService;
    }


    @Override
    public void save(StrategyComment comment) {
        // 获取当前登录的用户信息
        String phone = JwtUtils.getSubject();
        // feign调用user-server获取用户信息
        UserinfoDTO userInfo = userInfoFeignService.findByPhone(phone);
        // 设置用户信息
        comment.setUserId(userInfo.getId());
        comment.setNickname(userInfo.getNickname());
        comment.setCity(userInfo.getCity());
        comment.setLevel(userInfo.getLevel());
        comment.setHeadImgUrl(userInfo.getHeadImgUrl());
        comment.setCreateTime(new Date());
        // 保存评论
        commentRepository.save(comment);

    }

    @Override
    public Page<StrategyComment> findPage(CommentQuery qo ) {
        //  1. 根据攻略 id 查询评论
        Criteria criteria = Criteria.where("strategyId").is(qo.getArticleId());
        Query query = Query.query(criteria);

        //  2. 配置分页参数
        query.skip(qo.getStart()).limit(qo.getSize());
        query.with(Sort.by(Sort.Direction.DESC,"createTime"));
        PageRequest pageable = PageRequest.of(qo.getCurrent() - 1, qo.getSize());
        query.with(pageable);

        //  3. 统计总数
        long total   = mongoTemplate.count(query, StrategyComment.class);
        if  (total == 0){
            return Page.empty();
        }

        //  4. 查询当前页数据
        List<StrategyComment> strategyComments = mongoTemplate.find(query, StrategyComment.class);

        //  5. 封装结果返回
        return new PageImpl<>(strategyComments,pageable ,total);
    }

    @Override
    public void likes(String id) {
        // 1. 获取当前登录用户
        String phone = JwtUtils.getSubject();
        LoginResponseUser user = redisJsonService.get(RedisEnum.USER_LOGIN_INFO_KEY, phone);
        if (user == null){
            throw new BusinessException(401,"用户未登录");
        }

        // 2. 根据评论id查询评论
        Optional<StrategyComment> optional = commentRepository.findById(id);
        StrategyComment strategyComment = optional.orElseThrow(() -> new BusinessException(400, "评论不存在"));

        // 3. 判断当前用户是否已经点赞
        List<Long> thumbuplist = strategyComment.getThumbuplist();
        if (thumbuplist.contains(user.getId())){
            // 4. 如果已经点赞,则取消点赞,数量-1,取消集合中的用户id
            strategyComment.setThumbupnum(strategyComment.getThumbupnum() - 1);
            strategyComment.getThumbuplist().remove(user.getId());
        }else {
            // 5. 如果没有点赞,则点赞,数量+1
            strategyComment.setThumbupnum(strategyComment.getThumbupnum() + 1);
            strategyComment.getThumbuplist().add(user.getId());
        }
        // 6. 保存评论
        commentRepository.save(strategyComment);
    }


    @Override
    public void delete(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void update(StrategyComment strategyComment) {
        commentRepository.save(strategyComment);
    }

    @Override
    public StrategyComment findById(String id) {
        Optional<StrategyComment> optional = commentRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<StrategyComment> findList() {
        return commentRepository.findAll();
    }


}
