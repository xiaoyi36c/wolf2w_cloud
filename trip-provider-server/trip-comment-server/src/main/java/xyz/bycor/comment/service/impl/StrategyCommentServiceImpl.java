package xyz.bycor.comment.service.impl;

import org.springframework.stereotype.Service;
import xyz.bycor.comment.domian.StrategyComment;
import xyz.bycor.comment.feign.UserInfoFeignService;
import xyz.bycor.comment.repository.StrategyCommentRepository;
import xyz.bycor.comment.service.StrategyCommentService;
import xyz.bycor.core.utils.JwtUtils;
import xyz.bycor.member.dto.UserinfoDTO;

import java.util.List;

@Service
public class StrategyCommentServiceImpl implements StrategyCommentService {

    private final StrategyCommentRepository commentRepository;

    private final UserInfoFeignService userInfoFeignService;

    public StrategyCommentServiceImpl(StrategyCommentRepository strategyCommentRepository, UserInfoFeignService userInfoFeignService) {
        this.commentRepository = strategyCommentRepository;
        this.userInfoFeignService = userInfoFeignService;
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
        // 保存评论
        commentRepository.save(comment);

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
