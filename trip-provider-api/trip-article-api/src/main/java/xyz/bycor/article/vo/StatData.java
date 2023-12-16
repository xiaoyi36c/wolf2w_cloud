package xyz.bycor.article.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatData {
    private Long id; //文章id
    private Integer viewnum;  //点击数

    private Integer replynum;  //攻略评论数

    private Integer favornum; //收藏数

    private Integer sharenum; //分享数

    private Integer thumbsupnum; //点赞个数

}
