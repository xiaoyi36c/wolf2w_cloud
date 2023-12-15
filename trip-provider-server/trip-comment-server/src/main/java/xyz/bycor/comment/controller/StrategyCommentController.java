package xyz.bycor.comment.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.auth.anno.RequiredLogin;
import xyz.bycor.comment.domian.StrategyComment;
import xyz.bycor.comment.qo.CommentQuery;
import xyz.bycor.comment.service.StrategyCommentService;
import xyz.bycor.core.response.R;

@RestController
@RequestMapping("/strategies/comments")
@Api(tags = "攻略评论相关接口")
@Slf4j
public class StrategyCommentController {

    private final StrategyCommentService commentService;

    public StrategyCommentController(StrategyCommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation("添加攻略评论")
    @PostMapping
    @RequiredLogin
    public R<?> save(StrategyComment strategyComment){
    log.info("添加攻略评论:{}",strategyComment);
    commentService.save(strategyComment);
    return R.ok();
    }

    @ApiOperation("分页查询攻略评论")
    @GetMapping("/page")
    public R<Page<StrategyComment>> findPage(CommentQuery qo){
        log.info("分页查询攻略评论");
        return R.ok(commentService.findPage(qo));
    }

    @ApiOperation("点赞或取消点赞 攻略评论")
    @PostMapping("/likes")
    @RequiredLogin
    public R<?> likes(String cid){
        log.info("点赞或取消点赞 攻略评论:{}",cid);
        commentService.likes(cid);
        return R.ok();
    }




}
