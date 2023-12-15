package xyz.bycor.comment.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.auth.anno.RequestUser;
import xyz.bycor.auth.anno.RequiredLogin;
import xyz.bycor.auth.vo.LoginResponseUser;
import xyz.bycor.comment.domian.TravelComment;
import xyz.bycor.comment.qo.CommentQuery;
import xyz.bycor.comment.service.TravelCommentService;
import xyz.bycor.core.response.R;

import java.util.List;

@RestController
@RequestMapping("/travels/comments")
@Api(tags = "游记评论相关接口")
@Slf4j
public class TravelCommentController {

    private final TravelCommentService commentService;

    public TravelCommentController(TravelCommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation("添加游记评论")
    @PostMapping
    @RequiredLogin
    public R<?> save(TravelComment comment, @RequestUser LoginResponseUser user){
    log.info("添加游记评论:{}",comment);
    commentService.save(comment,user);
    return R.ok();
    }

    @ApiOperation("分页查询游记评论")
    @GetMapping("/list")
    public R<List<TravelComment>> findPage(CommentQuery qo){
        log.info("分页查询游记评论");
        return R.ok(commentService.findList(qo));
    }





}
