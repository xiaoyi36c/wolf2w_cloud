package xyz.bycor.article.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class StrategyDTO implements Serializable {


    private Long id;

    private Integer viewnum;  //点击数

    private Integer replynum;  //攻略评论数

    private Integer favornum; //收藏数

    private Integer sharenum; //分享数

    private Integer thumbsupnum; //点赞个数

}