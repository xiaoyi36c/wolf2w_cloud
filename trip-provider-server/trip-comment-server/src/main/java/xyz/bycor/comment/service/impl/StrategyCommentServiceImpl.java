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
import xyz.bycor.comment.qo.StrategyCommentQuery;
import xyz.bycor.comment.repository.StrategyCommentRepository;
import xyz.bycor.comment.service.StrategyCommentService;
import xyz.bycor.core.utils.JwtUtils;
import xyz.bycor.member.dto.UserinfoDTO;

import java.util.Date;
import java.util.List;

@Service
public class StrategyCommentServiceImpl implements StrategyCommentService {

    private final StrategyCommentRepository commentRepository;
    private final UserInfoFeignService userInfoFeignService;

    private final MongoTemplate mongoTemplate;

    public StrategyCommentServiceImpl(StrategyCommentRepository strategyCommentRepository, UserInfoFeignService userInfoFeignService, MongoTemplate mongoTemplate) {
        this.commentRepository = strategyCommentRepository;
        this.userInfoFeignService = userInfoFeignService;
        this.mongoTemplate = mongoTemplate;
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
    public Page<StrategyComment> findPage(StrategyCommentQuery qo ) {
        //  1. 根据攻略 id 查询评论
        Criteria criteria = Criteria.where("strategyId").is(qo.getStrategyId());
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
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void update(StrategyComment strategyComment) {
        commentRepository.save(strategyComment);
    }

    @Override
    public StrategyComment findById(Long id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public List<StrategyComment> findList() {
        return commentRepository.findAll();
    }


}
