package xyz.bycor.article.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @TableName destination
 */
@TableName(value ="destination")
@Data
public class Destination implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;


    private String name;


    private String english;


    private String coverUrl;


    private String info;


    private String parentName;


    private Long parentId;

    @TableField(exist = false)
    private List<Destination> children = new ArrayList<>();


}