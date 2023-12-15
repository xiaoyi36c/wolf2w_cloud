package xyz.bycor.comment.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.comment.domian.StrategyComment;
import xyz.bycor.comment.service.StrategyCommentService;
import xyz.bycor.core.response.R;

@RestController
@RequestMapping("/strategies")
@Api(tags = "攻略评论相关接口")
@Slf4j
public class StrategyCommentController {

    private final StrategyCommentService commentService;

    public StrategyCommentController(StrategyCommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation("添加攻略评论")
    @PostMapping
    public R<?> save(StrategyComment strategyComment){
    log.info("添加攻略评论:{}",strategyComment);
    commentService.save(strategyComment);
    return R.ok();
    }




}
