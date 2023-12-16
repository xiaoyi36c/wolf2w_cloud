package xyz.bycor.comment.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.auth.anno.RequestUser;
import xyz.bycor.auth.anno.RequiredLogin;
import xyz.bycor.auth.vo.LoginResponseUser;
import xyz.bycor.comment.domian.StrategyComment;
import xyz.bycor.comment.qo.CommentQuery;
import xyz.bycor.comment.service.StrategyCommentService;
import xyz.bycor.core.event.StatDataEvent;
import xyz.bycor.core.response.R;
import xyz.bycor.core.utils.SpringContextUtil;

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
    public R<?> save(StrategyComment strategyComment, @RequestUser LoginResponseUser user) {
        log.info("添加攻略评论,评论信息:{},用户信息:{}", strategyComment, user);
        // 发布攻略的浏览事件
        StatDataEvent.Data data = new StatDataEvent.Data();
        data.setId(strategyComment.getStrategyId());
        data.setField("replynum");
        data.setValue(1);
        SpringContextUtil.publishEvent(new StatDataEvent(data));
        commentService.save(strategyComment, user);
        return R.ok();
    }

    @ApiOperation("分页查询攻略评论")
    @GetMapping("/page")
    public R<Page<StrategyComment>> findPage(CommentQuery qo) {
        log.info("分页查询攻略评论");
        return R.ok(commentService.findPage(qo));
    }

    @ApiOperation("点赞或取消点赞 攻略评论")
    @PostMapping("/likes")
    @RequiredLogin
    public R<?> likes(String cid, @RequestUser LoginResponseUser user) {
        log.info("点赞或取消点赞,评论id:{},用户信息:{}", cid, user);
        commentService.likes(cid, user);
        return R.ok();
    }


}
