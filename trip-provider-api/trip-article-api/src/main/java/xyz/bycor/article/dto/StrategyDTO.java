package xyz.bycor.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.bycor.article.domain.StrategyContent;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
public class StrategyDTO implements Serializable {


    private Long id;

    private Long destId;  //关联的目的地
    private String destName;

    private Long themeId; //关联主题
    private String themeName;

    private Long catalogId;  //关联的分类
    private String catalogName;

    private String title;  //标题

    private String subTitle; //副标题

    private String summary;  //内容摘要

    private String coverUrl;  //封面

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;  //创建时间

    private Integer isabroad ;  //是否是国外

    private Integer viewnum;  //点击数

    private Integer replynum;  //攻略评论数

    private Integer favornum; //收藏数

    private Integer sharenum; //分享数

    private Integer thumbsupnum; //点赞个数

    private Integer state ;  //状态


    private StrategyContent content; //攻略内容

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime; //最后更新时间

}